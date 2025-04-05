package com.hotnews.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hotnews.database.entity.Favorites
import com.hotnews.database.dao.FavoritesDao

/**
 * Created by thomas on 2/20/2025.
 */

@Database(
    entities = [Favorites::class],
    version = 1,
    exportSchema = false
)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun FavoritesDao(): FavoritesDao
}
