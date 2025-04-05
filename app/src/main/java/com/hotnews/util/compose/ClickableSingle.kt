package com.hotnews.util.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * Created by thomas on 2/20/2025.
 *
 *  example usage:
 *  Text(
 *  text = "Click Once",
 *  modifier = Modifier.clickableSingle { handleClick() }
 *  )
 *
 */
fun Modifier.clickableSingle(
    enabled: Boolean = true,
    interval: Long = 1000L,
    onClick: () -> Unit
): Modifier = composed {
    this.then(
        Modifier.clickable(enabled = enabled,
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = debounceFun(interval = interval) { onClick() })
    )
}