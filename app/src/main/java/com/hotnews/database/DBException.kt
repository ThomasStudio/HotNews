package com.hotnews.database

/**
 * Created by thomas on 2/26/2025.
 */

class DBException(
    val error: DBError,
    override val message: String
) : RuntimeException(message)
