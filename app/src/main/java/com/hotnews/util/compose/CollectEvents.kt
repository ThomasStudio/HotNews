package com.hotnews.util.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Created by thomas on 2/24/2025.
 */

@Composable
fun <T> CollectEvents(
    flow: Flow<T>,
    eventHandler: @Composable (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var currentEvent by remember { mutableStateOf<T?>(null) }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                currentEvent = it
            }
        }
    }

    val event = currentEvent

    if (event != null) {
        eventHandler(event)
        currentEvent = null
    }
}
