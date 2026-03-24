package com.hotnews.base.navigation

/**
 * Base class to generate route string
 * Used by ViewModel to generate route string for navigation
 */
abstract class Router {
    /**
     * Generates the route string for navigation
     */
    abstract fun getRoute(): String
    
    /**
     * Gets the route with parameters
     */
    open fun getRouteWithArgs(vararg args: Pair<String, Any>): String {
        val route = getRoute()
        val params = args.joinToString("&") { "${it.first}=${it.second}" }
        return if (params.isNotEmpty()) "$route?$params" else route
    }
}