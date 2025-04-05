package com.hotnews.database

/**
 * Created by thomas on 2/25/2025.
 */

enum class DBError(val code: Int) {
    CONSTRAINT_FAILED(409),
    CONNECTION_LOST(503),
    UNKNOWN_ERROR(500)
}