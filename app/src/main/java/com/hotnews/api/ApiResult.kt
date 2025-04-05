package com.hotnews.api

/**
 * Created by thomas on 2/22/2025.
 */


sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val message: String?) : ApiResult<Nothing>()
}