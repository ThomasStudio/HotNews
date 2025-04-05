package com.hotnews.ui.pages.weibo

import androidx.lifecycle.viewModelScope
import com.hotnews.api.ApiResult
import com.hotnews.api.data.HotItem
import com.hotnews.api.data.WeiboHot
import com.hotnews.repository.WeiboRepository
import com.hotnews.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by thomas on 3/2/2025.
 */


@HiltViewModel
class WeiboViewModel @Inject constructor(
    private val repository: WeiboRepository
) : BaseViewModel<WeiboViewModel.Event, WeiboViewModel.State>(State.Loading) {

    init {
        viewModelScope.launch {
            getData()
        }
    }

    private fun getData() = viewModelScope.launch {
        change(
            State.Result(repository.fetchNews())
        )
    }

    sealed class Event {
        data object Back : Event()
    }

    sealed class State {
        data object Loading : State()
        data class Result(val data: ApiResult<WeiboHot>) : State()
    }
}