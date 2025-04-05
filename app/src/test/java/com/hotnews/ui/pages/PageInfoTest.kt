package com.hotnews.ui.pages

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Created by thomas on 4/5/2025.
 */

class PageInfoTest {
    @Test
    fun route() {
        PageInfo.entries.forEach {
            println(it.route())

            if (it == PageInfo.WebView) {
                assertEquals("""${it.name}/{${it.getArgs()[0]}}/{${it.getArgs()[1]}}""", it.route())
            } else {
                assertEquals(it.name, it.route())
            }
        }
    }

}