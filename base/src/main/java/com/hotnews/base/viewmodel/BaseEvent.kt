package com.hotnews.base.viewmodel

/**
 * Created by thomas on 3/29/2026.
 */

interface BaseEvent

data class NavigateEvent(
    val route: String,
    val popUpToRoute: String? = null,
    val inclusive: Boolean = false,
    val launchSingleTop: Boolean = false,
) : BaseEvent

object BackEvent : BaseEvent
data class MessageEvent(val message: String) : BaseEvent
