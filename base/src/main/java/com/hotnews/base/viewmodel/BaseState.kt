package com.hotnews.base.viewmodel

/**
 * Created by thomas on 3/29/2026.
 */

open class BaseState<Data : BaseData, Error : BaseError>(
    open val status: Status<Data, Error> = Status.Loading
)

sealed class Status<out Data : BaseData, out Error : BaseError> {
    object Loading : Status<Nothing, Nothing>()
    data class Success<Data : BaseData>(val data: Data) : Status<Data, Nothing>()
    data class Error<Error : BaseError>(val error: Error) : Status<Nothing, Error>()
}

open class BaseData

open class BaseError(val message: String)
