package com.hotnews.api.data

import com.google.gson.annotations.SerializedName

/**
 * Created by thomas on 2/21/2025.
 */

// url = https://www.zhihu.com/api/v4/search/top_search
data class ZhihuTopSearch(
    val top_search: Words
)

data class Words(
    val words: List<TopSearchItem>
)

data class TopSearchItem(
    val query: String,
    val display_query: String,
    val uuid: String
)