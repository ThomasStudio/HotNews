package com.hotnews.base.viewmodel

/**
 * Created by thomas on 3/29/2026.
 */

interface BaseEvent

data class NavigateEvent(val route: String) : BaseEvent
object BackEvent : BaseEvent
data class MessageEvent(val message: String) : BaseEvent
