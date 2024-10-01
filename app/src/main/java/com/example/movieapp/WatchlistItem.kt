package com.example.movieapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistItem(
    @PrimaryKey val imdbID: String,
    val title: String,
    val year: String,
    val poster: String,
    val status: WatchStatus = WatchStatus.PLANNED
)

enum class WatchStatus {
    PLANNED,
    COMPLETED
}