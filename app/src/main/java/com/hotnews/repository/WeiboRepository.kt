package com.hotnews.repository

import com.hotnews.api.ApiResult
import com.hotnews.api.service.WeiboService
import com.hotnews.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton
import com.hotnews.api.ApiRepository
import com.hotnews.api.data.WeiboHot
import kotlinx.coroutines.withContext

/**
 * Created by thomas on 2/20/2025.
 */

@Singleton
open class WeiboRepository @Inject constructor(
    private val weiboService: WeiboService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ApiRepository() {
    suspend fun fetchNews(): ApiResult<WeiboHot> = withContext(dispatcher) {
        runApiCall { weiboService.getNews() }
    }
}