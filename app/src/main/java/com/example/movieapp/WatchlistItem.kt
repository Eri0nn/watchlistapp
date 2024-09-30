// WatchlistItem.kt
package com.example.movieapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "watchlist")
data class WatchlistItem(
    @PrimaryKey val imdbID: String,
    val title: String,
    val year: String,
    val poster: String,

)
