package com.hotnews.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by thomas on 2/25/2025.
 */

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

    @Update
    suspend fun update(entity: T): Int

    @Delete
    suspend fun delete(entity: T): Int
}