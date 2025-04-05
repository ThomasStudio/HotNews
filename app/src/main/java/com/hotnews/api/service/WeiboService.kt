package com.hotnews.api.service

import com.hotnews.api.ApiResponse
import com.hotnews.api.data.WeiboHot
import retrofit2.http.GET

/**
 * Created by thomas on 2/28/2025.
 */

interface WeiboService {
    @GET("weibohot")
    suspend fun getNews(): ApiResponse<WeiboHot>
}