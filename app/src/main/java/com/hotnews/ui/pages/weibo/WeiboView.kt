package com.hotnews.ui.pages.weibo

import android.util.Log
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
import androidx.navigation.NavController
import com.hotnews.ui.pages.Page
import com.hotnews.api.ApiResult
import com.hotnews.api.data.HotItem
import com.hotnews.ui.pages.weibo.WeiboViewModel.Event
import com.hotnews.ui.pages.weibo.WeiboViewModel.State
import com.hotnews.ui.theme.WeiboIndex
import com.hotnews.util.compose.TitleBar
import com.hotnews.util.compose.clickableSingle
import com.hotnews.util.urlEncode
import com.hotnews.viewmodel.CollectEvents
import com.hotnews.viewmodel.CollectState

/**
 * Created by thomas on 3/2/2025.
 */

@Composable
fun WeiboView(
    navController: NavController,
    page: Page = Page.Weibo,
    viewModel: WeiboViewModel = hiltViewModel()
) {
    viewModel.CollectEvents {
        when (it) {
            Event.Back -> navController.popBackStack()
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
                                    HotItem(item, modifier = Modifier.clickableSingle {
                                        Page.WebView.path(
                                            item.url.urlEncode(),
                                            item.title,
                                        )
                                            .let { url ->
                                                Log.i("navigate", "WeiboView: $url")
                                                navController.navigate(url)
                                            }
                                    })
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