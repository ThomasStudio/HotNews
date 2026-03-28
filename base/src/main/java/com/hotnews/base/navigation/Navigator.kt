package com.hotnews.base.navigation

import androidx.navigation.NavHostController
/**
 * Abstraction for navigation actions. Used by the View (Composables) to perform navigation.
 */
interface Navigator {
    fun navigate(route: String)
    fun back()
}

/**
 * Extension function to turn a NavHostController into a Navigator.
 */
fun NavHostController.asNavigator(): Navigator = object : Navigator {
    override fun navigate(route: String) {
        this@asNavigator.navigate(route)
    }

    override fun back() {
        this@asNavigator.popBackStack()
    }
}


