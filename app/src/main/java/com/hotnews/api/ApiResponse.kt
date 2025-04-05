package com.hotnews.api

import com.google.gson.annotations.SerializedName

/**
 * Created by thomas on 2/22/2025.
 */

open class ApiResponse<T>(
    @SerializedName("code") val statusCode: Int,
    @SerializedName("msg") val message: String?,
    @SerializedName("data") val data: T?
) {
    fun isSuccess() = statusCode == 200
}