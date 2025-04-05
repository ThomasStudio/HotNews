package com.hotnews.util.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.LENGTH_LONG
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Created by thomas on 3/4/2025.
 */

enum class ToastDuration {
    Short,
    Long
}

@Composable
fun Toast(msg: String, duration: ToastDuration = ToastDuration.Short) =
    android.widget.Toast.makeText(
        LocalContext.current, msg, when (duration) {
            ToastDuration.Short -> LENGTH_SHORT
            ToastDuration.Long -> LENGTH_LONG
        }
    ).show()

/**
 * TitleBar with a back button and title
 */
@Composable
fun TitleBar(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    showBack: Boolean = true,
    showTitle: Boolean = true,
    onBack: (() -> Unit)? = null,
) {
    Row(modifier = modifier) {
        if (showBack) {
            onBack?.let {
                Icon(
                    icon,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickableSingle { it() }
                        .padding(start = 5.dp)
                        .size(28.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }

        if (showTitle) {
            Text(
                title,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically)
            )
        }

    }
}