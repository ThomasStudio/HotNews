package com.hotnews.database

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseLockedException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.sql.SQLException
import org.junit.jupiter.api.Assertions.*

data class TestEntity(val id: Long = 0, val content: String)

interface TestDao : BaseDao<TestEntity> {
    fun observeData(): Flow<Int>
}

class TestRepository(dao: TestDao) : DBRepository<TestEntity, TestDao>(dao)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class DBRepositoryTest {

    private lateinit var repository: TestRepository
    private val mockDao: TestDao = mockk()

    @BeforeEach
    fun setup() {
        repository = TestRepository(mockDao)
    }

    @Test
    fun `insert should return success result`() = runTest {
        // Given
        val dao: TestDao = mockk()
        val repo = TestRepository(dao)
        val entity = TestEntity(content = "test")
        coEvery { dao.insert(entity) } returns 1L

        // When
        val result = repo.insert(entity)

        // Then
        coVerify(exactly = 1) { dao.insert(entity) }
        assertTrue(result is DBResult.Success)
        assertEquals(1L, (result as DBResult.Success).data)
    }

    @Test
    fun `insert should handle constraint exception`() = runTest {
        // Given
        val entity = TestEntity(content = "test")
        val exception = SQLiteConstraintException("Constraint failed")
        coEvery { mockDao.insert(entity) } throws exception

        // When
        val result = repository.insert(entity)

        // Then
        assertTrue(result is DBResult.Error)
        assertTrue((result as DBResult.Error).exception is DBException)
        assertEquals(DBError.CONSTRAINT_FAILED, (result.exception as DBException).error)
    }

    @Test
    fun `update should return affected row count`() = runTest {
        // Given
        val entity = TestEntity(id = 1, content = "updated")
        coEvery { mockDao.update(entity) } returns 1

        // When
        val result = repository.update(entity)

        // Then
        assertTrue(result is DBResult.Success)
        assertEquals(1, (result as DBResult.Success).data)
    }

    @Test
    fun `delete should handle database locked exception`() = runTest {
        // Given
        val entity = TestEntity(id = 1L, content = "test")
        val exception = SQLiteDatabaseLockedException()
        coEvery { mockDao.delete(entity) } throws exception

        // When
        val result = repository.delete(entity)

        // Then
        assertTrue(result is DBResult.Error)
        assertEquals(
            DBError.CONNECTION_LOST,
            ((result as DBResult.Error).exception as DBException).error
        )
    }

    @Test
    fun `insertAll should return list of row ids`() = runTest {
        // Given
        val entities = listOf(TestEntity(content = "1"), TestEntity(content = "2"))
        coEvery { mockDao.insert(any()) } returns 1L andThen 2L

        // When
        val result = repository.insertAll(*entities.toTypedArray())

        // Then
        assertTrue(result is DBResult.Success)
        assertEquals(listOf(1L, 2L), (result as DBResult.Success).data)
    }

    @Test
    fun `observe should emit success values`() = runTest {
        // Given
        val testData = listOf(1, 2, 3)
        coEvery { mockDao.observeData() } returns flow { testData.forEach { emit(it) } }

        // When
        val results = repository.observe { observeData() }.toList()

        // Then
        assertEquals(3, results.size)
        results.forEachIndexed { index, result ->
            assertTrue(result is DBResult.Success)
            assertEquals(index + 1, (result as DBResult.Success).data)
        }
    }

    @Test
    fun `observe should catch exceptions`() = runTest {
        // Given
        val exception = RuntimeException("Test error")
        coEvery { mockDao.observeData() } returns flow { throw exception }

        // When
        val result = repository.observe { observeData() }.toList().first()

        // Then
        assertTrue(result is DBResult.Error)
        assertEquals(exception, (result as DBResult.Error).exception)
    }

    @Test
    fun `should handle generic sql exceptions`() = runTest {
        // Given
        val exception = SQLException("Generic error", null, 999)
        coEvery { mockDao.insert(any()) } throws exception

        // When
        val result = repository.insert(TestEntity(content = "test"))

        // Then
        assertTrue(result is DBResult.Error)
        assertEquals(
            DBError.UNKNOWN_ERROR,
            ((result as DBResult.Error).exception as DBException).error
        )
    }

    @Test
    fun `should handle generic sql exceptions 19`() = runTest {
        // Given
        val exception = SQLException("Generic error", null, 19)
        coEvery { mockDao.insert(any()) } throws exception

        // When
        val result = repository.insert(TestEntity(content = "test"))

        // Then
        assertTrue(result is DBResult.Error)
        assertEquals(
            DBError.CONSTRAINT_FAILED,
            ((result as DBResult.Error).exception as DBException).error
        )
    }

    @Test
    fun `should handle unexpected exceptions`() = runTest {
        // Given
        val exception = IllegalArgumentException("Invalid argument")
        coEvery { mockDao.update(any()) } throws exception

        // When
        val result = repository.update(TestEntity(content = "test"))

        // Then
        assertTrue(result is DBResult.Error)
        assertEquals(
            DBError.UNKNOWN_ERROR,
            ((result as DBResult.Error).exception as DBException).error
        )
    }
}