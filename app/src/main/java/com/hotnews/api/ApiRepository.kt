package com.hotnews.api

import okio.IOException
import retrofit2.HttpException

/**
 * Created by thomas on 2/22/2025.
 */

abstract class ApiRepository {
    suspend fun <T> runApiCall(
        apiCall: suspend () -> ApiResponse<T>
    ): ApiResult<T> {
        return try {
            val response = apiCall()
            when {
                response.isSuccess() && response.data != null ->
                    ApiResult.Success(response.data)

                response.isSuccess() ->
                    ApiResult.Error(204, "Empty response data")

                else ->
                    ApiResult.Error(response.statusCode, response.message ?: "Business error")
            }
        } catch (e: IOException) {
            ApiResult.Error(-1, "Network error: ${e.message}")
        } catch (e: HttpException) {
            ApiResult.Error(e.code(), "HTTP error: ${e.response()?.errorBody()?.string()}")
        } catch (e: Exception) {
            ApiResult.Error(-2, "System error: ${e.message}")
        }
    }
}