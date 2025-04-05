package com.hotnews.ui.pages.sites

import com.hotnews.ui.pages.PageInfo
import com.hotnews.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by thomas on 3/3/2025.
 */

@HiltViewModel
class SitesViewModel @Inject constructor() :
    BaseViewModel<SitesViewModel.Event, SitesViewModel.State>(State.Loading) {

    fun route(route: PageInfo) {
        send(Event.Route(route.path()))
    }

    sealed class Event {
        data object Back : Event()
        data class Route(val route: String) : Event()
    }

    sealed class State {
        data object Loading : State()

    }
}