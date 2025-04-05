package com.hotnews.database.entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by thomas on 4/6/2025.
 */

class FavoritesTest {
    @Test
    fun testFavorites() {
        val favorites = Favorites(
            id = 1,
            title = "Test Title",
            url = "https://example.com",
            addedTime = 1L
        )

        assertEquals(1, favorites.id)
        assertEquals("Test Title", favorites.title)
        assertEquals("https://example.com", favorites.url)
        assertEquals(1L, favorites.addedTime)
    }
}