package com.hotnews.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by thomas on 2/20/2025.
 */

@Entity(
    tableName = "favorites",
    indices = [Index("url", unique = true)]
)
data class Favorites(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val url: String,
    val addedTime: Long
)
