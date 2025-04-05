package com.hotnews.repository

import com.hotnews.database.DBRepository
import com.hotnews.database.dao.FavoritesDao
import com.hotnews.database.entity.Favorites
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by thomas on 2/20/2025.
 */

@Singleton
open class FavoritesRepository @Inject constructor(
    dao: FavoritesDao
) : DBRepository<Favorites, FavoritesDao>(dao) {
    val allBooks = observe { getAll() }
}

