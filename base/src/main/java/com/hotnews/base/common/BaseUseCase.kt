package com.hotnews.base.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers

/**
 * Abstract base class for implementing use cases
 * @param <P> Input parameter type
 * @param <T> Output result type
 */
abstract class BaseUseCase<in P, out T> {

    /**
     * Execute the use case
     * @param params Input parameters
     * @return Flow of result
     */
    operator fun invoke(params: P): Flow<T> = execute(params)
        .catch { emit(getErrorFlow(it)) }
        .flowOn(Dispatchers.IO)

    /**
     * Execute the use case without parameters
     * @return Flow of result
     */
    operator fun invoke(): Flow<T> = executeWithoutParams()
        .catch { emit(getErrorFlow(it)) }
        .flowOn(Dispatchers.IO)

    /**
     * Execute the use case with parameters
     * @param params Input parameters
     * @return Flow of result
     */
    protected abstract fun execute(params: P): Flow<T>

    /**
     * Execute the use case without parameters
     * @return Flow of result
     */
    protected open fun executeWithoutParams(): Flow<T> {
        throw NotImplementedError("executeWithoutParams() not implemented")
    }

    /**
     * Handle errors in the flow
     * @param throwable Error to handle
     * @return Error flow item
     */
    protected abstract fun getErrorFlow(throwable: Throwable): T
}