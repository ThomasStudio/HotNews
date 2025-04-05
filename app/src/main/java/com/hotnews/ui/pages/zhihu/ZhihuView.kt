package com.hotnews.ui.pages.zhihu

/**
 * Created by thomas on 3/5/2025.
 */

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hotnews.ui.pages.Page
import com.hotnews.api.ApiResult
import com.hotnews.api.data.ZhihuTarget
import com.hotnews.viewmodel.CollectEvents
import com.hotnews.viewmodel.CollectState
import com.hotnews.ui.pages.zhihu.ZhihuViewModel.Event
import com.hotnews.ui.pages.zhihu.ZhihuViewModel.State
import com.hotnews.util.compose.TitleBar
import com.hotnews.util.compose.clickableSingle
import com.hotnews.util.urlEncode

@Composable
fun ZhihuView(
    navController: NavController,
    page: Page = Page.Zhihu,
    viewModel: ZhihuViewModel = hiltViewModel()
) {
    viewModel.CollectEvents {
        when (it) {
            Event.Back -> navController.popBackStack()
            else -> Unit
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TitleBar(page.title) {
            viewModel.send(Event.Back)
        }

        viewModel.CollectState {
            when (it) {
                is State.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                is State.Result -> {
                    when (it.data) {
                        is ApiResult.Success -> {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1.0f)
                                    .fillMaxWidth()
                            ) {
                                items(it.data.data.data) { item ->
                                    Item(
                                        item.target,
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .clickableSingle {
                                                Page.WebView.path(
                                                    item.target.link.urlEncode(),
                                                    item.target.title,
                                                )
                                                    .let { url ->
                                                        Log.i("navigate", "WeiboView: $url")
                                                        navController.navigate(url)
                                                    }

                                            }
                                    )
                                }
                            }
                        }

                        is ApiResult.Error -> {
                            Text("${it.data.code} : ${it.data.message}")
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun Item(item: ZhihuTarget, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                item.title,
                color = Color.Blue,
                fontSize = 22.sp
            )
        }
    }
}