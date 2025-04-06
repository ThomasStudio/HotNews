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
            if (it == PageInfo.WebView) {
                assertEquals("""${it.name}/{${it.getArgs()[0]}}/{${it.getArgs()[1]}}""", it.route())
            } else {
                assertEquals(it.name, it.route())
            }
        }
    }

    @Test
    fun path() {
        PageInfo.entries.forEach {
            if (it == PageInfo.WebView) {
                val params = arrayOf<String>("1", "2")
                assertEquals(
                    """${it.name}/${params[0]}/${params[1]}""",
                    it.path(* params)
                )
            } else {
                assertEquals(it.name, it.path())
            }
        }
    }

    @Test
    fun getArgs() {
        PageInfo.entries.forEach {
            if (it == PageInfo.WebView) {
                assertEquals(listOf("url", "title"), it.getArgs())
            } else {
                assertEquals(emptyList<String>(), it.getArgs())
            }
        }
    }

}