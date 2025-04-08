package com.hotnews.repository

import com.hotnews.database.DBResult
import com.hotnews.database.dao.FavoritesDao
import com.hotnews.database.entity.Favorites
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesRepositoryTest {

    private lateinit var repository: FavoritesRepository
    private lateinit var mockDao: FavoritesDao

    @Test
    fun `allBooks should handle empty data`() = runTest {
        mockDao = mockk {
            coEvery { getAll() } returns flowOf(emptyList())
        }
        repository = FavoritesRepository(mockDao)

        val results = repository.allBooks.toList()

        assertTrue(results[0] is DBResult.Success)
        assertTrue((results[0] as DBResult.Success).data.isEmpty())
    }

    @Test
    fun `allBooks should return actual data`() = runTest {
        val testData = listOf(createTestFavorite(1))
        mockDao = mockk {
            coEvery { getAll() } returns flowOf(testData)
        }
        repository = FavoritesRepository(mockDao)

        coEvery { mockDao.getAll() } returns flow { emit(testData) }

        val results = repository.allBooks.firstOrNull()

        assertEquals(testData, (results as DBResult.Success).data)
    }


    private fun createTestFavorite(id: Long = 0): Favorites {
        return Favorites(
            id = id,
            title = "Test Title $id",
            url = "https://test.com/$id",
            addedTime = System.currentTimeMillis()
        )
    }
}