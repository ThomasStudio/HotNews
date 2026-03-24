package com.hotnews.base.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

/**
 * Base class to generate NavGraph
 * It uses Router to generate route string and then add composable to NavGraph with route string
 * It will be used in MainActivity to set up NavHost
 */
abstract class NavGraph {
    /**
     * Builds the navigation graph
     */
    abstract fun createGraph(
        navController: NavHostController,
        navGraphBuilder: NavGraphBuilder
    )
    
    /**
     * Adds a composable screen to the navigation graph
     */
    protected fun NavGraphBuilder.addScreen(
        router: Router,
        content: @androidx.compose.animation.core.Animatable<androidx.compose.ui.Modifier>.() -> androidx.compose.runtime.Composable
    ) {
        composable(router.getRoute()) {
            content()
        }
    }
    
    /**
     * Adds a composable screen with arguments to the navigation graph
     */
    protected fun NavGraphBuilder.addScreenWithArgs(
        router: Router,
        arguments: List<androidx.navigation.NamedNavArgument> = emptyList(),
        content: @androidx.compose.animation.core.Animatable<androidx.compose.ui.Modifier>.() -> androidx.compose.runtime.Composable
    ) {
        composable(
            route = router.getRoute(),
            arguments = arguments
        ) {
            content()
        }
    }
}