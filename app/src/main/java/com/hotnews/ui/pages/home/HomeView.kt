package com.hotnews.ui.pages.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hotnews.feature.weibo.ui.WeiboView
import com.hotnews.feature.zhihu.ui.ZhihuView
import com.hotnews.ui.pages.PageInfo
import com.hotnews.ui.pages.favorites.FavoritesView
import com.hotnews.ui.pages.sites.SitesView
import com.hotnews.ui.pages.webview.WebViewView
//import com.hotnews.ui.pages.weibo.WeiboView
//import com.hotnews.ui.pages.zhihu.ZhihuView

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
        modifier = modifier, navController = navController, startDestination = PageInfo.Sites.route()
    ) {
        composable(PageInfo.Sites.route()) {
            SitesView(navController)
        }
        composable(PageInfo.Weibo.route()) {
            WeiboView(navController)
        }
        composable(PageInfo.WebView.route()) { backStackEntry ->
            val url = backStackEntry.arguments?.getString(PageInfo.WebView.getArgs()[0])
            val title = backStackEntry.arguments?.getString(PageInfo.WebView.getArgs()[1])

            url?.let {
                WebViewView(it, title = title ?: "", navController = navController)
            }
        }
        composable(PageInfo.Favorites.route()) {
            FavoritesView(navController)
        }
        composable(PageInfo.Zhihu.route()) {
            ZhihuView(navController)
        }
    }

}
