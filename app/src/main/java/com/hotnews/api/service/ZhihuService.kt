package com.hotnews.api.service

import com.hotnews.api.ApiResponse
import com.hotnews.api.data.ZhihuHot
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by thomas on 2/21/2025.
 */


interface ZhihuService {
    @GET("{type}?limit=50")
    suspend fun getHot(@Path("type") type: String): ApiResponse<ZhihuHot>

    enum class Types(val path: String, val title: String) {
        Total("total", "全站"),
        Science("science", "科学"),
        Digital("digital", "数码"),
        Sport("sport", "体育"),
        Fashion("fashion", "时尚"),
        Film("film", "影视")
    }
}