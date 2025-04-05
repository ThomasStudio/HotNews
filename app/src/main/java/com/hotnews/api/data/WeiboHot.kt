package com.hotnews.api.data

import com.google.gson.annotations.SerializedName

/**
 * Created by thomas on 2/28/2025.
 * https://v2.xxapi.cn/api/weibohot
 */

data class WeiboHot(
    val code: Int,
    val msg: String,
    val data: List<HotItem>
)

data class HotItem(
    val hot: String,
    val index: Int,
    val title: String,
    val url: String
)
