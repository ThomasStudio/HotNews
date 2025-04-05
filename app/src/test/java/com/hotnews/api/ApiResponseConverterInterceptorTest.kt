package com.hotnews.api

import io.mockk.every
import io.mockk.mockk
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.IOException

class ApiResponseConverterInterceptorTest {

    private val interceptor = ApiResponseConverterInterceptor()

    private fun mockChainWithResponse(response: Response): Interceptor.Chain {
        val chain = mockk<Interceptor.Chain>()
        val request = Request.Builder().url("http://test.com").build()
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        return chain
    }

    @Test
    fun `normal response`() {
        val originalJson = """{"name":"John","age":30}"""
        val response = Response.Builder()
            .request(Request.Builder().url("http://test.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(originalJson.toResponseBody("application/json".toMediaType()))
            .build()


        val processedResponse = interceptor.intercept(mockChainWithResponse(response))


        assertEquals(200, processedResponse.code)
        val body = processedResponse.body?.string()?.let { JSONObject(it) }
        assertNotNull(body)
        assertEquals(200, body?.getInt("code"))
        assertEquals("success", body?.getString("msg"))
        assertEquals("John", body?.getJSONObject("data")?.getString("name"))
    }

    @Test
    fun `empty response return 500 error`() {

        val response = Response.Builder()
            .request(Request.Builder().url("http://test.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("".toResponseBody("application/json".toMediaType()))
            .build()


        val processedResponse = interceptor.intercept(mockChainWithResponse(response))


        assertEquals(500, processedResponse.code)
        val body = processedResponse.body?.string()?.let { JSONObject(it) }
        assertEquals(500, body?.getInt("code"))
        assertEquals("Parse error", body?.getString("msg"))
    }

    @Test
    fun `error json return 500 error`() {

        val response = Response.Builder()
            .request(Request.Builder().url("http://test.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("invalid json".toResponseBody("application/json".toMediaType()))
            .build()


        val processedResponse = interceptor.intercept(mockChainWithResponse(response))


        assertEquals(500, processedResponse.code)
        val body = processedResponse.body?.string()?.let { JSONObject(it) }
        assertEquals(500, body?.getInt("code"))
    }

    @Test
    fun `IO exception return 500 error`() {

        val errorBody = mockk<ResponseBody>()
        every { errorBody.source() } throws IOException("Test error")

        val response = Response.Builder()
            .request(Request.Builder().url("http://test.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(errorBody)
            .build()


        val processedResponse = interceptor.intercept(mockChainWithResponse(response))


        assertEquals(500, processedResponse.code)
        val body = processedResponse.body?.string()?.let { JSONObject(it) }
        assertEquals(500, body?.getInt("code"))
    }

    @Test
    fun `Non json response return 500 error`() {

        val response = Response.Builder()
            .request(Request.Builder().url("http://test.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("plain text".toResponseBody("text/plain".toMediaType()))
            .build()


        val processedResponse = interceptor.intercept(mockChainWithResponse(response))


        assertEquals(500, processedResponse.code)
    }

    @Test
    fun `response body is null return 500 error`() {

        val response = Response.Builder()
            .request(Request.Builder().url("http://test.com").build())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(null)
            .build()

        val processedResponse = interceptor.intercept(mockChainWithResponse(response))

        assertEquals(500, processedResponse.code)
    }
}