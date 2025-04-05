package com.hotnews

import com.hotnews.ui.pages.Page
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * Created by thomas on 3/2/2025.
 */

class PageKtTest {

    @Test
    fun route() {
        Page.entries.forEach {
            println(it.route())
            assertEquals(it.route(), it.name)
        }
    }
}