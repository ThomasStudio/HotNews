package com.hotnews.base.navigation

/**
 * Created by thomas on 3/29/2026.
 */

interface AppRoute {
    fun route(): String

    companion object {
        /**
         * Builds a route from path segments (e.g., "home", "detail" -> "home/detail").
         */
        fun create(vararg path: String): String = path.joinToString("/")

        /**
         * Replaces placeholders in a route template with actual values.
         * @param baseRoute route template (e.g., "detail/{id}")
         * @param params map of placeholder to value (e.g., mapOf("id" to "123"))
         * @return fully built route string (e.g., "detail/123")
         */
        fun createWithParams(baseRoute: String, params: Map<String, String>): String {
            var route = baseRoute
            params.forEach { (key, value) ->
                route = route.replace("{$key}", value)
            }
            return route
        }
    }
}
