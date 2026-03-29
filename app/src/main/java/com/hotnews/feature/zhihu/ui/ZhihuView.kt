package com.hotnews.feature.zhihu.ui

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
import androidx.navigation.NavHostController
import com.hotnews.api.data.ZhihuTarget
import com.hotnews.base.navigation.asNavigator
import com.hotnews.base.viewmodel.Status
import com.hotnews.base.viewmodel.collectUiState
import com.hotnews.base.viewmodel.handleEvents
import com.hotnews.feature.zhihu.viewmodel.ZhihuContract
import com.hotnews.feature.zhihu.viewmodel.ZhihuViewModel
import com.hotnews.ui.pages.PageInfo
import com.hotnews.util.compose.TitleBar
import com.hotnews.util.compose.clickableSingle
import android.util.Log
/**
 * Created by thomas on 3/5/2025.
 */

@Composable
fun ZhihuView(
    navController: NavHostController,
    page: PageInfo = PageInfo.Zhihu,
    viewModel: ZhihuContract = hiltViewModel<ZhihuViewModel>()
) {
    Log.d("ZhihuView", "ZhihuView compose")

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
                        Item(
                            item.target,
                            modifier = Modifier
                                .padding(5.dp)
                                .clickableSingle {
                                }
                        )
                    }
                }
            }

            is Status.Error -> {
                Text("${uiState.status.error.code} : ${uiState.status.error.message}")
            }

            /*
            is Status.Success -> {
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
                                            viewModel.openUrl(item.target)
                                        }
                                )
                            }
                        }
                    }

                    is ApiResult.Error -> {
                        Text("${it.data.code} : ${it.data.message}")
                    }
                }
            }
            */

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