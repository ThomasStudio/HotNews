package com.hotnews.base.navigation

import com.hotnews.base.util.StringUtil

/**
 * Created by thomas on 3/29/2026.
 */

abstract class AppRoute {
    abstract val path: String

    fun child(vararg segments: String): AppRoute = Route(routeString(path, *segments))
    operator fun div(segments: String): AppRoute = child(segments)

    fun withQuery(vararg params: Pair<String, Any?>): String = routeWithQuery(path, *params)

    data class Route(override val path: String) : AppRoute() {
        override fun toString(): String = path
    }


    companion object {
        fun route(vararg segments: String): AppRoute = Route(routeString(*segments))

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
