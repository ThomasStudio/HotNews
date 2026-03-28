package com.hotnews.base.viewmodel

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BaseContract {
    val uiState: StateFlow<BaseState<*, *>>
    val event: SharedFlow<BaseEvent>
    fun back()
}
