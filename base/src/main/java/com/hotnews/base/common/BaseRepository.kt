package com.hotnews.base.common

/**
 * Base Repository interface defining common operations
 */
interface BaseRepository {
    /**
     * Perform cleanup operations
     */
    suspend fun cleanup()
}