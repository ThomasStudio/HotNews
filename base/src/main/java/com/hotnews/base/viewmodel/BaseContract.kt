package com.hotnews.base.viewmodel

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BaseContract<STATE : BaseStateIF> {
    val uiState: StateFlow<STATE>
    val event: SharedFlow<BaseEvent>
    fun back()
}