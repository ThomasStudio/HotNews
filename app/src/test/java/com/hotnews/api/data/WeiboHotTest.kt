package com.hotnews.api.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by thomas on 4/5/2025.
 */

class WeiboHotTest {
    @Test
    fun testWeiboHot() {
        val weiboHot = WeiboHot(
            code = 200,
            msg = "success",
            data = listOf(
                HotItem(hot = "热搜1", index = 1, title = "标题1", url = "http://example.com/1"),
                HotItem(hot = "热搜2", index = 2, title = "标题2", url = "http://example.com/2")
            )
        )

        assertEquals(200, weiboHot.code)
        assertEquals("success", weiboHot.msg)
        assertEquals(2, weiboHot.data.size)
        assertEquals("热搜1", weiboHot.data[0].hot)
        assertEquals(1, weiboHot.data[0].index)
        assertEquals("标题1", weiboHot.data[0].title)
        assertEquals("http://example.com/1", weiboHot.data[0].url)
    }
}