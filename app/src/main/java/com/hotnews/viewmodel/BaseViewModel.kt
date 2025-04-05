package com.hotnews.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.hotnews.util.compose.CollectEvents
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 */

/**
 * Created by thomas on 3/2/2025.
 *
 * To use this base class, subclass should declare Event and State, for example
 * class VM : BaseViewModel<WeiboViewModel.Event, WeiboViewModel.State>(State.Loading) {
 *     sealed class Event {
 *         data object Back : Event()
 *     }
 *
 *     sealed class State {
 *         data object Loading : State()
 *         data class Result(val data: ApiResult<WeiboHot>) : State()
 *     }
 * }
 *
 */
abstract class BaseViewModel<Event, State>(initState: State) : ViewModel() {
    /**
     * event for UI, such as click button ,show toast etc.
     * send it and composable should do something accordingly
     */
    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    /**
     * UI state, it should include necessary data for UI to show
     * change it and UI should update
     */
    private val _uiState = MutableStateFlow(initState)
    val uiState: StateFlow<State> = _uiState

    /**
     * send event to composable
     */
    fun send(event: Event) = viewModelScope.launch {
        _events.emit(event)
    }

    /**
     * change uiState
     */
    protected fun change(state: State) {
        viewModelScope.launch {
            _uiState.value = state
        }
    }
}

@Composable
fun <Event, State> BaseViewModel<Event, State>.CollectEvents(handler: @Composable (event: Event) -> Unit) {
    CollectEvents(events) {
        handler(it)
    }
}

@Composable
fun <Event, State> BaseViewModel<Event, State>.CollectState(handler: @Composable (state: State) -> Unit) {
    val uiState by this.uiState.collectAsStateWithLifecycle()

    uiState?.let { handler(it) }
}