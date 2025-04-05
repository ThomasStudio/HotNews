package com.hotnews.api.service

import com.hotnews.api.ApiResponse
import com.hotnews.api.data.ZhihuTopSearch
import retrofit2.http.GET

/**
 * Created by thomas on 3/5/2025.
 */

interface ZhihuTopSearchService {
    @GET("top_search")
    suspend fun getItems(): ApiResponse<ZhihuTopSearch>
}