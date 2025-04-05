package com.hotnews.database

/**
 * Created by thomas on 2/24/2025.
 */


sealed class DBResult<out T> {
    data class Success<out T>(val data: T) : DBResult<T>()
    data class Error(val exception: Throwable) : DBResult<Nothing>()
    data object Loading : DBResult<Nothing>()
}