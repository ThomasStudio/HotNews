package com.hotnews.database

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by thomas on 4/6/2025.
 */

class DBErrorTest {
    @Test
    fun testDBError() {
        val error = DBError.CONSTRAINT_FAILED
        assertEquals(409, error.code)
    }

    @Test
    fun testDBErrorConnectionLost() {
        val error = DBError.CONNECTION_LOST
        assertEquals(503, error.code)
    }

    @Test
    fun testDBErrorUnknown() {
        val error = DBError.UNKNOWN_ERROR
        assertEquals(500, error.code)
    }
}