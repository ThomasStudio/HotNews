package com.hotnews.database.dao

/**
 * Created by thomas on 2/20/2025.
 */

import androidx.room.*
import com.hotnews.database.BaseDao
import com.hotnews.database.entity.Favorites
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao : BaseDao<Favorites> {
    @Query("SELECT * FROM favorites ORDER BY id DESC")
    fun getAll(): Flow<List<Favorites>>
}
