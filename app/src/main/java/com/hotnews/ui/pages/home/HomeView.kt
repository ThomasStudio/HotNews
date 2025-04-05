package com.hotnews.ui.pages.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hotnews.ui.pages.Page
import com.hotnews.ui.pages.favorites.FavoritesView
import com.hotnews.ui.pages.sites.SitesView
import com.hotnews.ui.pages.webview.WebViewView
import com.hotnews.ui.pages.weibo.WeiboView
import com.hotnews.ui.pages.zhihu.ZhihuView

/**
 * Created by thomas on 3/2/2025.
 */

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier, navController = navController, startDestination = Page.Sites.route()
    ) {
        composable(Page.Sites.route()) {
            SitesView(navController)
        }
        composable(Page.Weibo.route()) {
            WeiboView(navController)
        }
        composable(Page.WebView.route()) { backStackEntry ->
            val url = backStackEntry.arguments?.getString(Page.WebView.getArgs()[0])
            val title = backStackEntry.arguments?.getString(Page.WebView.getArgs()[1])

            url?.let {
                WebViewView(it, title = title ?: "", navController = navController)
            }
        }
        composable(Page.Favorites.route()) {
            FavoritesView(navController)
        }
        composable(Page.Zhihu.route()) {
            ZhihuView(navController)
        }
    }

}