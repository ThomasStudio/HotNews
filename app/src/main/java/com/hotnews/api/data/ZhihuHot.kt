package com.hotnews.api.data

import android.util.Log
import com.hotnews.api.BaseUrl

/**
 * Created by thomas on 3/5/2025.
 */

data class ZhihuHot(val data: List<ZhihuHotItem>)

data class ZhihuHotItem(
    val type: String,
    val style_type: String,
    val id: String,
    val card_id: String,
    val target: ZhihuTarget,
    val attached_info: String,
    val detail_text: String,
    val trend: Int,
    val debut: Boolean,
    val children: List<Children>
)


data class Children(val type: String, val thumbnail: String)


data class ZhihuTarget(
    val id: Long,
    val title: String,
    val url: String,
    val type: String,
    val created: Long,
    val answer_count: Int,
    val follower_count: Int,
    val author: Author,
    val bound_topic_ids: List<Int>,
    val comment_count: Int,
    val is_following: Boolean,
    val excerpt: String
) {
    val link: String
        get() {
            return BaseUrl.ZhihuQuestion.url + id
        }

}

data class Author(
    val type: String,
    val user_type: String,
    val id: String,
    val url_token: String,
    val url: String,
    val name: String,
    val headline: String,
    val avatar_url: String
)