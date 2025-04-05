package com.hotnews.api

/**
 * Created by thomas on 3/5/2025.
 */

enum class BaseUrl(val url: String) {
    Zhihu("https://www.zhihu.com/api/v3/feed/topstory/hot-lists/"),
    ZhihuQuestion("https://www.zhihu.com/question/"),
    Weibo("https://v2.xxapi.cn/api/"),
}
