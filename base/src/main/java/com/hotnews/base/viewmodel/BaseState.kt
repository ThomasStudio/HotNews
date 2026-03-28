package com.hotnews.base.viewmodel

/**
 * Created by thomas on 3/29/2026.
 */
open class BaseState<D : BaseData, E : BaseError>(
    open val status: Status<D, E> = Status.Loading
)

sealed class Status<out D : BaseData, out E : BaseError> {
    object Loading : Status<Nothing, Nothing>()
    data class Success<D : BaseData>(val data: D) : Status<D, Nothing>()
    data class Error<E : BaseError>(val error: E) : Status<Nothing, E>()
}

open class BaseData

open class BaseError(val message: String)
