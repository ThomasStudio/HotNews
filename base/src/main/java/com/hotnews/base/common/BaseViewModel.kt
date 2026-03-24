package com.hotnews.base.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class with generic State and Event types
 * Provides basic functionality for handling UI states and events in MVVM architecture
 *
 * To use this base class, subclass should declare State and Event, for example
 * class MyViewModel : BaseViewModel<MyState, MyEvent>(MyState.Initial) {
 *     // ViewModel implementation
 * }
 */
abstract class BaseViewModel<State, Event> : ViewModel() {
    /**
     * UI State flow for managing the UI state
     */
    private val _uiState: MutableStateFlow<State>
    val uiState: StateFlow<State> = _uiState

    /**
     * Event flow for UI interactions, such as navigation, showing toasts, etc.
     */
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event> = _event

    constructor(initialState: State) : super() {
        _uiState = MutableStateFlow(initialState)
    }

    /**
     * Updates the UI state
     */
    protected fun updateState(newState: State) {
        _uiState.value = newState
    }

    /**
     * Sends an event to be handled by the UI
     */
    protected fun send(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }
}

/**
 * Composable function to collect state from BaseViewModel
 */
@Composable
fun <State, Event> BaseViewModel<State, Event>.collectState(): State {
    val uiState by this.uiState.collectAsStateWithLifecycle()
    return uiState
}

/**
 * Composable function to collect events from BaseViewModel
 */
@Composable
fun <State, Event> BaseViewModel<State, Event>.collectEvents(
    onEvent: (Event) -> Unit
) {
    val events by this.event.collectAsStateWithLifecycle()
    
    // Side effect to handle events
    androidx.compose.runtime.LaunchedEffect(Unit) {
        this@collectEvents.event.collect { event ->
            onEvent(event)
        }
    }
}