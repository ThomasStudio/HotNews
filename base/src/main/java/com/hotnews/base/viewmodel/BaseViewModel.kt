package com.hotnews.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by thomas on 3/29/2026.
 */
abstract class BaseViewModel<STATE : BaseState<*, *>> : ViewModel(), BaseContract<STATE> {

    private val _uiState = MutableStateFlow(initialState())
    override val uiState: StateFlow<STATE> = _uiState

    private val _event = MutableSharedFlow<BaseEvent>()
    override val event: SharedFlow<BaseEvent> = _event.asSharedFlow()

    protected abstract fun initialState(): STATE

    protected fun updateState(state: STATE) {
        updateState { state }
    }

    protected fun updateState(reducer: STATE.() -> STATE) {
        _uiState.update(reducer)
    }

    protected fun send(viewEvent: BaseEvent) {
        if (_event.tryEmit(viewEvent)) return

        viewModelScope.launch { _event.emit(viewEvent) }
    }

    override fun back() {
        send(BackEvent)
    }

    protected fun sendMessage(message: String) {
        send(MessageEvent(message))
    }

    protected fun navigate(route: String) {
        send(NavigateEvent(route))
    }

}
