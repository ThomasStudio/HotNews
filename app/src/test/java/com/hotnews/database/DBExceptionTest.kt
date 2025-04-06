package com.hotnews.database

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by thomas on 4/6/2025.
 */

class DBExceptionTest {
    @Test
    fun testDBException() {
        val error = DBError.CONSTRAINT_FAILED
        val exception = DBException(error, "Test message")
        assertEquals(error, exception.error)
        assertEquals("Test message", exception.message)
    }

    @Test
    fun testDBExceptionConnectionLost() {
        val error = DBError.CONNECTION_LOST
        val exception = DBException(error, "Connection lost")
        assertEquals(error, exception.error)
        assertEquals("Connection lost", exception.message)
    }

    @Test
    fun testDBExceptionUnknown() {
        val error = DBError.UNKNOWN_ERROR
        val exception = DBException(error, "Unknown error")
        assertEquals(error, exception.error)
        assertEquals("Unknown error", exception.message)
    }

}