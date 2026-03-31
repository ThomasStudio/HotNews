package com.hotnews.base.viewmodel

/**
 * Created by thomas on 3/29/2026.
 */

data class BaseState<DATA>(
    override val status: Status = Status.Loading,
    override val data: DATA? = null,
    override val error: BaseError? = null
) : BaseStateIF


interface BaseStateIF {
    val status: Status
    val data: Any?
    val error: BaseError?
}

enum class Status {
    Loading, Success, Error
}


open class BaseError(val code: Int = -1, val message: String = "")
