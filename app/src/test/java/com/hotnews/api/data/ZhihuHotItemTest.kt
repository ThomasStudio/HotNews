package com.hotnews.api.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by thomas on 4/5/2025.
 */

class ZhihuHotItemTest {
    @Test
    fun testZhihuHotItem() {
        val zhihuHotItem = ZhihuHotItem(
            type = "hot",
            style_type = "normal",
            id = "123456",
            card_id = "654321",
            target = ZhihuTarget(
                id = 123456789,
                title = "Sample Title",
                url = "http://example.com",
                type = "question",
                created = 1622547800,
                answer_count = 10,
                follower_count = 100,
                author = Author(
                    type = "user",
                    user_type = "normal",
                    id = "987654321",
                    url_token = "sample_user",
                    url = "http://example.com/user",
                    name = "Sample User",
                    headline = "Sample Headline",
                    avatar_url = "http://example.com/avatar.jpg"
                ),
                bound_topic_ids = listOf(1, 2, 3),
                comment_count = 5,
                is_following = false,
                excerpt = "Sample excerpt"
            ),
            attached_info = "Sample info",
            detail_text = "Sample detail text",
            trend = 1,
            debut = true,
            children = listOf(Children(type = "child", thumbnail = "http://example.com/child.jpg"))
        )

        assertEquals("hot", zhihuHotItem.type)
        assertEquals("normal", zhihuHotItem.style_type)
        assertEquals("123456", zhihuHotItem.id)
        assertEquals("654321", zhihuHotItem.card_id)
        assertNotNull(zhihuHotItem.target)
        assertEquals("Sample Title", zhihuHotItem.target.title)
        assertEquals(123456789, zhihuHotItem.target.id)
        assertEquals("http://example.com", zhihuHotItem.target.url)
        assertEquals(10, zhihuHotItem.target.answer_count)
        assertEquals(100, zhihuHotItem.target.follower_count)
        assertEquals("Sample User", zhihuHotItem.target.author.name)
        assertEquals(5, zhihuHotItem.target.comment_count)
    }
}