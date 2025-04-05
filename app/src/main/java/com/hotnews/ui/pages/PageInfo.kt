package com.hotnews.ui.pages

/**
 * Created by thomas on 3/2/2025.
 */

/**
 * Page enum class for navigation in NavHost
 */
enum class PageInfo(val title: String = "") {
    Weibo("Weibo"),
    Zhihu("Zhihu"),
    Sites("Hot News"),
    Favorites("Favorites"),
    WebView;

    /**
     * used for NavHost
     */
    fun route() = when (this) {
        WebView -> {
            """${this.name}/{${getArgs().first()}}/{${getArgs()[1]}}"""
        }

        else -> {
            name
        }
    }

    /**
     * used for real route path in composable fun
     */
    fun path(vararg values: String) = when (this) {
        WebView -> {
            // two args, url and title
            """${name}/${values[0]}/${values[1]}"""
        }

        else -> {
            name
        }
    }

    fun getArgs() = when (this) {
        WebView -> {
            listOf("url", "title")
        }

        else -> {
            emptyList()
        }
    }
}



