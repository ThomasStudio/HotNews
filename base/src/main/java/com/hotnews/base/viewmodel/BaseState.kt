package com.hotnews.base.viewmodel

/**
 * Created by thomas on 3/29/2026.
 */

open class BaseState<State : BaseData, Event : BaseError>(
    open val status: Status<State, Event> = Status.Loading
)

sealed class Status<out State : BaseData, out Event : BaseError> {
    object Loading : Status<Nothing, Nothing>()
    data class Success<State : BaseData>(val data: State) : Status<State, Nothing>()
    data class Error<Event : BaseError>(val error: Event) : Status<Nothing, Event>()
}

open class BaseData

open class BaseError(val message: String)
