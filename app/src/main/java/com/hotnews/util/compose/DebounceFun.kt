package com.hotnews.util.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Created by thomas on 2/20/2025.
 *
 * usage:
 *
 * Button(
 *     onClick = debounceClick { onBack() }
 * ) {
 *     Text("Go back")
 * }
 */
const val INTERVAL = 1000L

@Composable
inline fun debounceFun(
    interval: Long = INTERVAL,
    crossinline func: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(value = 0L) }

    return {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > interval) {
            func()
            lastClickTime = currentTimeMillis
        }
    }
}