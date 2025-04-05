package com.hotnews.api

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by thomas on 4/6/2025.
 */

class ApiResponseTest {
    @Test
    fun `testApiResponse success`() {
        val apiResponse = ApiResponse(
            statusCode = 200,
            message = "Success",
            data = "Sample Data"
        )

        assertEquals(200, apiResponse.statusCode)
        assertEquals("Success", apiResponse.message)
        assertEquals("Sample Data", apiResponse.data)
        assertTrue(apiResponse.isSuccess())
    }

    @Test
    fun `testApiResponse with error`() {
        val apiResponse = ApiResponse(
            statusCode = 500,
            message = "Success",
            data = "Sample Data"
        )

        assertEquals(500, apiResponse.statusCode)
        assertEquals("Success", apiResponse.message)
        assertEquals("Sample Data", apiResponse.data)
        assertTrue(!apiResponse.isSuccess())
    }
}