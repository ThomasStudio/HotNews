package com.hotnews.ui.pages.favorites

/**
 * Created by thomas on 3/4/2025.
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hotnews.database.DBResult
import com.hotnews.viewmodel.CollectEvents
import com.hotnews.viewmodel.CollectState
import com.hotnews.ui.pages.favorites.FavoritesViewModel.Event
import com.hotnews.ui.pages.favorites.FavoritesViewModel.State
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hotnews.ui.pages.PageInfo
import com.hotnews.database.entity.Favorites
import com.hotnews.util.compose.TitleBar
import com.hotnews.util.compose.clickableSingle
import com.hotnews.util.toTimeStr
import com.hotnews.util.urlEncode

@Composable
fun FavoritesView(
    navController: NavController,
    route: PageInfo = PageInfo.Favorites,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    viewModel.CollectEvents {
        when (it) {
            Event.Back -> navController.popBackStack()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TitleBar(route.title) { viewModel.send(Event.Back) }


        viewModel.CollectState {
            when (it) {
                is State.Data -> {
                    when (it.data) {
                        is DBResult.Success -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                items(it.data.data) { item ->
                                    Item(item, modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                        .clickableSingle {
                                            navController.navigate(
                                                PageInfo.WebView.path(
                                                    item.url.urlEncode(),
                                                    item.title,
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        is DBResult.Error -> {

                        }

                        else -> Unit
                    }
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun Item(item: Favorites, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                item.title,
                color = Color.Blue,
                fontSize = 22.sp
            )
            Text(item.addedTime.toTimeStr())
        }
    }
}