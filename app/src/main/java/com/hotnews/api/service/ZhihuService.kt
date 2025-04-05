package com.hotnews.api.service

import com.hotnews.api.ApiResponse
import com.hotnews.api.data.ZhihuHot
import retrofit2.http.GET

/**
 * Created by thomas on 2/21/2025.
 */


interface ZhihuService {
    @GET("total?limit=50")
    suspend fun getHot(): ApiResponse<ZhihuHot>
}