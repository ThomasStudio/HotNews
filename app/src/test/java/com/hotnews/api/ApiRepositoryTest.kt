package com.hotnews.api

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response

class ApiRepositoryTest {

    private val mockApiCall: suspend () -> ApiResponse<Any> = mockk()
    private val repository = object : ApiRepository() {} // 创建匿名子类以测试抽象类

    @Test
    fun `runApiCall should return Success when response is successful with data`() = runBlocking {
        val testData = "Test Data"
        coEvery { mockApiCall() } returns ApiResponse(
            statusCode = 200,
            message = "OK",
            data = testData
        )

        val result = repository.runApiCall(mockApiCall)


        assertTrue(result is ApiResult.Success)
        assertEquals(testData, (result as ApiResult.Success).data)
    }

    @Test
    fun `runApiCall should return Error 204 when response is successful but data is null`() =
        runBlocking {

            coEvery { mockApiCall() } returns ApiResponse(
                statusCode = 200,
                message = "OK",
                data = null
            )


            val result = repository.runApiCall(mockApiCall)


            assertTrue(result is ApiResult.Error)
            assertEquals(204, (result as ApiResult.Error).code)
            assertEquals("Empty response data", result.message)
        }

    @Test
    fun `runApiCall should return Business Error when response indicates failure`() = runBlocking {

        coEvery { mockApiCall() } returns ApiResponse(
            statusCode = 400,
            message = null,
            data = null
        )


        val result = repository.runApiCall(mockApiCall)


        assertTrue(result is ApiResult.Error)
        assertEquals(400, (result as ApiResult.Error).code)
        assertEquals("Business error", result.message)
    }

    @Test
    fun `runApiCall should return error message when response indicates failure`() = runBlocking {

        coEvery { mockApiCall() } returns ApiResponse(
            statusCode = 400,
            message = "error message",
            data = null
        )


        val result = repository.runApiCall(mockApiCall)


        assertTrue(result is ApiResult.Error)
        assertEquals(400, (result as ApiResult.Error).code)
        assertEquals("error message", result.message)
    }

    @Test
    fun `runApiCall should handle Network Error (IOException)`() = runBlocking {

        coEvery { mockApiCall() } throws IOException("No internet")


        val result = repository.runApiCall(mockApiCall)


        assertTrue(result is ApiResult.Error)
        assertEquals(-1, (result as ApiResult.Error).code)
        assertTrue(result.message!!.startsWith("Network error:"))
    }

    @Test
    fun `runApiCall should handle HTTP Error (HttpException)`() = runBlocking {

        val errorResponse = Response.error<Any>(
            500,
            "Server Error".toResponseBody(null)
        )
        coEvery { mockApiCall() } throws HttpException(errorResponse)


        val result = repository.runApiCall(mockApiCall)


        assertTrue(result is ApiResult.Error)
        assertEquals(500, (result as ApiResult.Error).code)
        assertEquals("HTTP error: Server Error", result.message)
    }

    @Test
    fun `runApiCall should handle HTTP Error (null response)`() = runBlocking {
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 500
        coEvery { httpException.response() } returns null

        coEvery { mockApiCall() } throws httpException


        val result = repository.runApiCall(mockApiCall)


        assertTrue(result is ApiResult.Error)
        assertEquals(500, (result as ApiResult.Error).code)
        assertEquals("HTTP error: null", result.message)
    }

    @Test
    fun `runApiCall should handle HTTP Error (null body)`() = runBlocking {
        val httpException = mockk<HttpException>()
        val response = mockk<Response<Any>>()
        coEvery { httpException.code() } returns 500
        coEvery { httpException.response() } returns response
        coEvery { response.errorBody() } returns null

        coEvery { mockApiCall() } throws httpException


        val result = repository.runApiCall(mockApiCall)


        assertTrue(result is ApiResult.Error)
        assertEquals(500, (result as ApiResult.Error).code)
        assertEquals("HTTP error: null", result.message)
    }

    @Test
    fun `runApiCall should handle Generic Exception`() = runBlocking {

        coEvery { mockApiCall() } throws IllegalStateException("Unexpected error")


        val result = repository.runApiCall(mockApiCall)


        assertTrue(result is ApiResult.Error)
        assertEquals(-2, (result as ApiResult.Error).code)
        assertTrue(result.message!!.startsWith("System error:"))
    }
}
