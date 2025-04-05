package com.hotnews.di

import android.content.Context
import androidx.room.Room
import com.hotnews.database.db.FavoritesDatabase
import com.hotnews.database.dao.FavoritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by thomas on 2/20/2025.
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFavoritesDatabase(
        @ApplicationContext context: Context
    ): FavoritesDatabase = Room.databaseBuilder(
        context,
        FavoritesDatabase::class.java,
        "favorites.db"
    ).build()

    @Provides
    fun provideFavoritesDao(database: FavoritesDatabase): FavoritesDao = database.FavoritesDao()
}