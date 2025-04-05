package com.hotnews.ui.pages.home

import com.hotnews.api.service.ZhihuService
import com.hotnews.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by thomas on 3/2/2025.
 */

@HiltViewModel
class HomeViewModel @Inject constructor() :
    BaseViewModel<HomeViewModel.Event, HomeViewModel.State>(State.Loading) {

    sealed class Event {
    }

    sealed class State {
        data object Loading : State()
    }
}