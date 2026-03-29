package com.hotnews.base.navigation

import androidx.navigation.NavHostController

/**
 * Abstraction for navigation actions. Used by the View (Composables) to perform navigation.
 */
interface Navigator {
    fun navigate(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        launchSingleTop: Boolean
    )

    fun back()
}

fun Navigator.navigate(route: String) = navigate(route, null, false, false)

/**
 * Extension function to turn a NavHostController into a Navigator.
 */
fun NavHostController.asNavigator(): Navigator = object : Navigator {
    override fun navigate(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        launchSingleTop: Boolean
    ) {
        navigate(route) {
            popUpToRoute?.let { popUpRoute ->
                popUpTo(popUpRoute) { this.inclusive = inclusive }
            }
            this.launchSingleTop = launchSingleTop
        }
    }

    override fun back() {
        popBackStack()
    }
}

/**
 * Default implementation of Navigator that does nothing. Useful for previews or when navigation is not needed.
 */
class DefaultNavigator : Navigator {
    override fun navigate(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        launchSingleTop: Boolean
    ) {
    }

    override fun back() {}
}
