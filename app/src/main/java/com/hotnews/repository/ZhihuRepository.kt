package com.hotnews.repository

import com.hotnews.api.ApiResult
import com.hotnews.api.ApiRepository
import com.hotnews.api.data.ZhihuHot
import com.hotnews.api.service.ZhihuService.Types
import com.hotnews.api.service.ZhihuService
import com.hotnews.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by thomas on 3/5/2025.
 */

@Singleton
open class ZhihuRepository @Inject constructor(
    private val service: ZhihuService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ApiRepository() {
    suspend fun getHot(type: Types): ApiResult<ZhihuHot> = withContext(dispatcher) {
        runApiCall { service.getHot(type.path) }
    }
}