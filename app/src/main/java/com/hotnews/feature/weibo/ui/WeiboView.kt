package com.hotnews.feature.weibo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.hotnews.api.data.HotItem
import com.hotnews.base.navigation.asNavigator
import com.hotnews.base.viewmodel.Status
import com.hotnews.base.viewmodel.collectUiState
import com.hotnews.base.viewmodel.handleEvents
import com.hotnews.feature.weibo.viewmodel.WeiboContract
import com.hotnews.feature.weibo.viewmodel.WeiboViewModel
import com.hotnews.ui.pages.PageInfo
import com.hotnews.ui.theme.WeiboIndex
import com.hotnews.util.compose.TitleBar
import com.hotnews.util.compose.clickableSingle
import android.util.Log

/**
 * Created by thomas on 3/29/2026.
 */

@Composable
fun WeiboView(
    navController: NavHostController,
    page: PageInfo = PageInfo.Weibo,
    viewModel: WeiboContract = hiltViewModel<WeiboViewModel>()
) {
    Log.d("WeiboView", "WeiboView compose")

    val uiState = viewModel.collectUiState()
    viewModel.handleEvents(navController.asNavigator())

    Column(modifier = Modifier.fillMaxSize()) {
        TitleBar(page.title) {
            viewModel.back()
        }

        when (uiState.status) {
            Status.Loading -> {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            is Status.Success<*> -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxWidth()
                ) {
                    val data = (uiState.status as Status.Success).data.data
                    items(data.data) { item ->
                        HotItem(
                            item,
                            modifier = Modifier.clickableSingle {
                                viewModel.openUrl(item)
                            }
                        )
                    }
                }
            }

            is Status.Error -> {
                Text("${uiState.status.error.code} : ${uiState.status.error.message}")
            }
        }
    }
}

@Composable
fun HotItem(item: HotItem, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Text(item.index.toString(), color = WeiboIndex, modifier = Modifier.weight(0.1f))
        Text(item.title, modifier = Modifier.weight(1f))
    }
}
