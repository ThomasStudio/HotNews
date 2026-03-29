package com.hotnews.feature.weibo.viewmodel

import androidx.lifecycle.viewModelScope
import com.hotnews.api.ApiResult.Error
import com.hotnews.api.ApiResult.Success
import com.hotnews.base.viewmodel.BaseError
import com.hotnews.base.viewmodel.BaseViewModel
import com.hotnews.base.viewmodel.Status
import com.hotnews.repository.WeiboRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import com.hotnews.api.data.HotItem
import com.hotnews.ui.pages.PageInfo
import com.hotnews.util.urlEncode

/**
 * Created by thomas on 3/29/2026.
 */

@HiltViewModel
class WeiboViewModel @Inject constructor(
    val repository: WeiboRepository,
) : BaseViewModel<WeiboState>(), WeiboContract {

    override fun initialState() = WeiboState()

    init {
        Log.d("WeiboViewModel", "WeiboViewModel init")
        getData()
    }

    private fun getData() =
        viewModelScope.launch {
            when (val data = repository.fetchNews()) {
                is Success -> {
                    Log.d("WeiboViewModel", "WeiboViewModel get data success")
                    updateState {
                        WeiboState(Status.Success(WeiboData(data.data)))
                    }
                }

                is Error -> {
                    Log.d("WeiboViewModel", "WeiboViewModel get data error: ${data.message}")
                    updateState {
                        WeiboState(
                            Status.Error(
                                BaseError(
                                    data.code,
                                    data.message ?: "unknown error"
                                )
                            )
                        )
                    }
                }
            }
        }

    override fun openUrl(item: HotItem) {
        navigate(
            PageInfo.WebView.path(
                item.url.urlEncode(),
                item.title,
            )
        )
    }
}
