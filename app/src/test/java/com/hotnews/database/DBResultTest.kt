package com.hotnews.database

import io.mockk.mockk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.IOException
import java.sql.SQLException


class DBResultTest {

    @Nested
    @DisplayName("Success Cases")
    inner class SuccessCases {
        @Test
        fun `should hold correct data`() {
            // Given
            val testData = "Test Success"

            // When
            val result = DBResult.Success(testData)

            // Then
            assertEquals(result.data, testData)
        }

        @Test
        fun `should have value-based equality`() {
            val result1 = DBResult.Success(100)
            val result2 = DBResult.Success(100)

            assertEquals(result1, result2)
            assertEquals(result1.hashCode(), result2.hashCode())
        }

        @Test
        fun `should not equal different types`() {
            val success = DBResult.Success(42)
            val error = DBResult.Error(Throwable())

            assertEquals(success, success)
            assertNotEquals(success, error)
        }
    }

    @Nested
    @DisplayName("Error Cases")
    inner class ErrorCases {
        @Test
        fun `should wrap throwable correctly`() {
            // Given
            val mockException: Throwable = mockk()

            // When
            val result = DBResult.Error(mockException)

            // Then
            assertEquals(result.exception, mockException)

        }

        @Test
        fun `should handle different exception types`() {
            val ioException = DBResult.Error(IOException())
            val nullPointer = DBResult.Error(NullPointerException())

            assertNotEquals(ioException, nullPointer)
        }
    }

    @Nested
    @DisplayName("Loading Case")
    inner class LoadingCase {
        @Test
        fun `should be singleton instance`() {
            val loading1 = DBResult.Loading
            val loading2 = DBResult.Loading

            assertSame(loading1, loading2)
        }

        @Test
        fun `should have correct string representation`() {
            assertEquals(DBResult.Loading.toString(), "Loading")
        }
    }

    @Test
    fun `should provide correct type checks`() {
        val success = DBResult.Success("data")
        val error = DBResult.Error(RuntimeException())
        val loading = DBResult.Loading

        assertEquals(success::class, DBResult.Success::class)
        assertEquals(error::class, DBResult.Error::class)
        assertEquals(loading::class, DBResult.Loading::class)
    }

    @Test
    fun `should correctly unwrap success data`() {
        val expected = 3.14
        val result = DBResult.Success(expected)

        when (result) {
            is DBResult.Success -> assertEquals(result.data, expected)
            else -> throw AssertionError("Wrong result type")
        }
    }

    @Test
    fun `should handle multiple error types`() {
        val results = listOf(
            DBResult.Error(IllegalStateException()),
            DBResult.Error(IOException()),
            DBResult.Error(SQLException())
        )

        results.forEach { result ->
            assertTrue(result is DBResult.Error)
        }
    }
}