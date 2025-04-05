package com.hotnews.ui.pages.sites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hotnews.ui.pages.PageInfo
import com.hotnews.ui.pages.sites.SitesViewModel.Event
import com.hotnews.util.compose.CollectEvents
import com.hotnews.util.compose.TitleBar
import com.hotnews.util.compose.clickableSingle

/**
 * Created by thomas on 3/3/2025.
 */

@Composable
fun SitesView(
    navController: NavHostController,
    page: PageInfo = PageInfo.Sites,
    viewModel: SitesViewModel = hiltViewModel()
) {
    CollectEvents(viewModel.events) {
        when (it) {
            is Event.Route -> {
                navController.navigate(it.route)
            }

            Event.Back -> {
                navController.popBackStack()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TitleBar(page.title)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(PageInfo.entries) {
                if (it != page && it.title.isNotEmpty()) {
                    RouteItem(it,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .fillMaxWidth()
                            .clickableSingle { viewModel.route(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun RouteItem(route: PageInfo, modifier: Modifier) {
    Card(
        modifier = modifier
    ) {
        Text(
            route.title,
            color = Color.Magenta,
            fontSize = 26.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}