package com.hotnews.di

import android.content.Context
import com.hotnews.R
import com.hotnews.api.ApiResponseConverterInterceptor
import com.hotnews.api.BaseUrl
import com.hotnews.api.service.WeiboService
import com.hotnews.api.service.ZhihuService
import com.hotnews.api.service.ZhihuTopSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

/**
 * Created by thomas on 2/21/2025.
 */


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ZhihuTopSearchRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ZhihuRetrofit


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeiboRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ) = OkHttpClient.Builder()
        .addInterceptor(ApiResponseConverterInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (context.resources.getBoolean(R.bool.isDebug)) {
                level = Level.BODY
            }
        })
        .build()


    @ZhihuRetrofit
    @Provides
    fun provideZhihuRetrofit(okHttpClient: OkHttpClient) =
        getRetrofit(BaseUrl.Zhihu.url, okHttpClient)

    @Provides
    fun provideZhihuService(@ZhihuRetrofit retrofit: Retrofit) =
        retrofit.create(ZhihuService::class.java)


    @ZhihuTopSearchRetrofit
    @Provides
    fun provideZhihuTopSearchRetrofit(okHttpClient: OkHttpClient) =
        getRetrofit(BaseUrl.ZhihuTopSearch.url, okHttpClient)

    @Provides
    fun provideZhihuTopService(@ZhihuTopSearchRetrofit retrofit: Retrofit) =
        retrofit.create(ZhihuTopSearchService::class.java)

    @WeiboRetrofit
    @Provides
    fun provideWeiboRetrofit(okHttpClient: OkHttpClient) =
        getRetrofit(BaseUrl.Weibo.url, okHttpClient)

    @Provides
    fun provideWeiboService(@WeiboRetrofit retrofit: Retrofit) =
        retrofit.create(WeiboService::class.java)

}

private fun getRetrofit(baseUrl: String, okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()