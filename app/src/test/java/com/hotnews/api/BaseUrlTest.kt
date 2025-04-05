package com.hotnews.api

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class BaseUrlTest {

    @Test
    fun `should have exactly three enum entries`() {
        val entries = enumValues<BaseUrl>()
        assertThat(entries).hasLength(3)
    }

    @Test
    fun `Zhihu entry should have correct properties`() {
        val entry = BaseUrl.Zhihu
        assertThat(entry.name).isEqualTo("Zhihu")
        assertThat(entry.url).isEqualTo("https://www.zhihu.com/api/v3/feed/topstory/hot-lists/")
    }

    @Test
    fun `ZhihuQuestion entry should have correct properties`() {
        val entry = BaseUrl.ZhihuQuestion
        assertThat(entry.name).isEqualTo("ZhihuQuestion")
        assertThat(entry.url).isEqualTo("https://www.zhihu.com/question/")
    }

    @Test
    fun `Weibo entry should have correct properties`() {
        val entry = BaseUrl.Weibo
        assertThat(entry.name).isEqualTo("Weibo")
        assertThat(entry.url).isEqualTo("https://v2.xxapi.cn/api/")
    }

    @Test
    fun `entries should be in declared order`() {
        val entries = enumValues<BaseUrl>()
        assertThat(entries.map { it.name })
            .containsExactly("Zhihu", "ZhihuQuestion", "Weibo")
            .inOrder()
    }
}