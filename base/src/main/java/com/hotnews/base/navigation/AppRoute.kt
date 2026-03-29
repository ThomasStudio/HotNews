package com.hotnews.base.navigation

import com.hotnews.base.util.StringUtil

/**
 * Created by thomas on 3/29/2026.
 */

data class AppRoute(val path: String) {

    fun child(vararg segments: String): AppRoute =
        AppRoute(routeString(this.path, *segments))

    operator fun div(segments: String): AppRoute = child(segments)

    fun withQuery(vararg params: Pair<String, Any?>): String =
        routeWithQuery(path, *params)

    companion object {
        fun create(vararg segments: String): AppRoute =
            AppRoute(routeString(*segments))

        fun routeString(vararg segments: String): String =
            segments.asSequence()
                .map { it.trim('/') }
                .filter { it.isNotBlank() }
                .joinToString("/")

        fun routeWithQuery(path: String, vararg params: Pair<String, Any?>): String {
            val normalizedPath = routeString(path)
            val query = params
                .asSequence()
                .mapNotNull { (key, value) ->
                    if (value == null) return@mapNotNull null
                    val safeKey = key.trim()
                    if (safeKey.isBlank()) return@mapNotNull null
                    "${StringUtil.encode(safeKey)}=${StringUtil.encode(value.toString())}"
                }
                .joinToString("&")
            return if (query.isBlank()) normalizedPath else "$normalizedPath?$query"
        }
    }
}
