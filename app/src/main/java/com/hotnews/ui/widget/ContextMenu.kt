package com.hotnews.ui.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Created by thomas on 3/4/2025.
 */

data class MenuItem(
    val title: String,
    val action: () -> Unit
)

@Composable
fun ContextMenu(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.MoreVert,
    actions: List<MenuItem>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(icon, contentDescription = "Context menu")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            actions.forEach {
                DropdownMenuItem(
                    text = { Text(it.title) },
                    onClick = {
                        expanded = false
                        it.action()
                    }
                )
            }
        }
    }


}