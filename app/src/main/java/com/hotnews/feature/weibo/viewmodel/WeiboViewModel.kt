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
        fetchData()
    }

    private fun fetchData() =
        viewModelScope.launch {
            when (val result = repository.fetchNews()) {
                is Success -> updateState {
                    copy(status = Status.Success(WeiboData(result.data)))
                }

                is Error -> updateState {
                    copy(
                        status = Status.Error(
                            BaseError(
                                result.code,
                                result.message ?: "Unknown error"
                            )
                        )
                    )
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
