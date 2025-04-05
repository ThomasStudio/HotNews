package com.hotnews.database

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseLockedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.VisibleForTesting
import java.sql.SQLException

/**
 * Created by thomas on 2/24/2025.
 */

abstract class DBRepository<Entity : Any, DAO : BaseDao<Entity>>(
    @VisibleForTesting internal val dao: DAO
) {
    suspend fun insert(entity: Entity): DBResult<Long> = safeExecute {
        dao.insert(entity)
    }

    suspend fun update(entity: Entity): DBResult<Int> = safeExecute {
        dao.update(entity)
    }

    suspend fun delete(entity: Entity): DBResult<Int> = safeExecute {
        dao.delete(entity)
    }

    suspend fun insertAll(vararg entities: Entity): DBResult<List<Long>> = safeExecute {
        entities.map { dao.insert(it) }
    }

    fun <T> observe(query: DAO.() -> Flow<T>): Flow<DBResult<T>> = dao.query()
        .map<T, DBResult<T>> { DBResult.Success(it) }
        .catch { emit(DBResult.Error(it)) }
        .flowOn(Dispatchers.IO)

    private suspend fun <T> safeExecute(
        operation: suspend () -> T
    ): DBResult<T> = withContext(Dispatchers.IO) {
        try {
            DBResult.Success(operation())
        } catch (e: Exception) {
            DBResult.Error(translateException(e))
        }
    }

    private fun translateException(e: Exception): Throwable = when (e) {
        is SQLiteConstraintException -> DBException(
            DBError.CONSTRAINT_FAILED,
            "Constraint failed: ${e.message}"
        )

        is SQLiteDatabaseLockedException -> DBException(
            DBError.CONNECTION_LOST,
            "Database locked"
        )

        is SQLException -> when (e.errorCode) {
            19 -> DBException(
                DBError.CONSTRAINT_FAILED,
                e.message ?: "Database constraint failed"
            )

            else -> DBException(
                DBError.UNKNOWN_ERROR,
                "SQL Error: ${e.errorCode}"
            )
        }

        else -> DBException(
            DBError.UNKNOWN_ERROR,
            "Unexpected error: ${e.message}"
        )
    }
}