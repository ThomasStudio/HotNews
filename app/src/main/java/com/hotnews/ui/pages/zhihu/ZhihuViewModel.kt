package com.hotnews.ui.pages.zhihu

/**
 * Created by thomas on 3/5/2025.
 */

import androidx.lifecycle.viewModelScope
import com.hotnews.api.ApiResult
import com.hotnews.api.data.ZhihuHot
import com.hotnews.api.data.ZhihuTarget
import com.hotnews.repository.ZhihuRepository
import com.hotnews.ui.pages.PageInfo
import com.hotnews.util.urlEncode
import com.hotnews.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ZhihuViewModel @Inject constructor(
    val repository: ZhihuRepository,
) :
    BaseViewModel<ZhihuViewModel.Event, ZhihuViewModel.State>(State.Loading) {

    init {
        viewModelScope.launch {
            getData()
        }
    }

    private fun getData() = viewModelScope.launch {
        change(
            State.Result(repository.getHot())
        )
    }

    fun openUrl(target: ZhihuTarget) {
        send(
            Event.Route(
                PageInfo.WebView.path(
                    target.link.urlEncode(),
                    target.title,
                )
            )
        )
    }


    sealed class Event {
        data object Back : Event()
        data class Route(val route: String) : Event()
    }

    sealed class State {
        data object Loading : State()
        data class Result(val data: ApiResult<ZhihuHot>) : State()
    }
}