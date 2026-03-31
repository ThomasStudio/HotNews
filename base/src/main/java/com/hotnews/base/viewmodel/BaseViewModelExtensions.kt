package com.hotnews.base.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.hotnews.base.navigation.Navigator
import kotlinx.coroutines.flow.collectLatest

/**
 * Created by thomas on 3/29/2026.
 */

@Composable
fun <STATE : BaseStateIF> BaseContract<STATE>.collectUiState(): STATE {
    val uiState by this.uiState.collectAsStateWithLifecycle()
    return uiState
}

@Composable
fun <STATE : BaseStateIF> BaseContract<STATE>.handleEvents(
    navigator: Navigator? = null,
    onEvent: (BaseEvent) -> Unit = {},
) {
    val lifecyclerOwner = LocalLifecycleOwner.current

    LaunchedEffect(this, lifecyclerOwner, navigator) {
        lifecyclerOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            event.collectLatest { viewEvent ->
                when (viewEvent) {
                    is NavigateEvent -> {
                        navigator?.navigate(
                            route = viewEvent.route,
                            popUpToRoute = viewEvent.popUpToRoute,
                            inclusive = viewEvent.inclusive,
                            launchSingleTop = viewEvent.launchSingleTop
                        )
                    }

                    is BackEvent -> navigator?.back()
                    else -> onEvent(viewEvent)
                }
            }
        }
    }
}