package com.hotnews.base.navigation

import androidx.navigation.NavController

/**
 * Base class to navigate with route string
 * It uses NavHostController to navigate with route string
 */
class Navigator {
    private var navController: NavController? = null
    
    fun bind(navController: NavController) {
        this.navController = navController
    }
    
    fun unbind() {
        this.navController = null
    }
    
    /**
     * Navigate to the given route
     */
    fun navigate(route: String) {
        navController?.navigate(route)
    }
    
    /**
     * Navigate back
     */
    fun navigateUp() {
        navController?.navigateUp()
    }
    
    /**
     * Pop back stack to the given route
     */
    fun popBackStack(route: String, inclusive: Boolean = false) {
        navController?.popBackStack(route, inclusive)
    }
    
    /**
     * Check if navigator is bound
     */
    fun isBound(): Boolean = navController != null
}