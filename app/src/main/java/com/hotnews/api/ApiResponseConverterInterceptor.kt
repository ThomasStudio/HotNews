package com.hotnews.api

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

/**
 * Created by thomas on 2/23/2025.
 */

class ApiResponseConverterInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        return try {
            // get original JSON
            val source = response.body?.source()
            val buffer = source?.let {
                it.request(Long.MAX_VALUE)
                it.buffer
            }
            val originalJson = buffer?.readUtf8()

            // build BaseResponse
            val wrappedJson = JSONObject().apply {
                put("code", 200)
                put("msg", "success")
                put("data", JSONObject(originalJson ?: "")) // data is original JSON
            }.toString()

            response.newBuilder()
                .body(wrappedJson.toResponseBody(response.body?.contentType()))
                .build()
        } catch (e: Exception) {
            // handle exception
            response.newBuilder()
                .code(500)
                .body("{\"code\":500,\"msg\":\"Parse error\"}".toResponseBody("application/json".toMediaType()))
                .build()
        }
    }
}
