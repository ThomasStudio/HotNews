package com.hotnews.base.viewmodel

/**
 * Created by thomas on 3/29/2026.
 */

open class BaseState<DATA : BaseData, ERROR : BaseError>(
    open val status: Status<DATA, ERROR> = Status.Loading
) {
    constructor (data: DATA) : this(status = Status.Success(data))
    constructor (error: ERROR) : this(status = Status.Error(error))
}

sealed class Status<out DATA : BaseData, out ERROR : BaseError> {
    object Loading : Status<Nothing, Nothing>()
    data class Success<DATA : BaseData>(val data: DATA) : Status<DATA, Nothing>()
    data class Error<ERROR : BaseError>(val error: ERROR) : Status<Nothing, ERROR>()
}

interface BaseData

open class BaseError(val code: Int = -1, val message: String = "")
