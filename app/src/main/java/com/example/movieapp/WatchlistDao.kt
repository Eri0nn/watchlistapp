package com.example.movieapp

import androidx.room.*
import com.example.movieapp.data.WatchlistItem
import com.example.movieapp.data.WatchStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist")
    fun getAllWatchlistItems(): Flow<List<WatchlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlistItem(item: WatchlistItem)

    @Delete
    suspend fun deleteWatchlistItem(item: WatchlistItem)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE imdbID = :imdbID)")
    suspend fun isInWatchlist(imdbID: String): Boolean

    @Query("UPDATE watchlist SET status = :status WHERE imdbID = :imdbID")
    suspend fun updateWatchStatus(imdbID: String, status: WatchStatus)

    @Query("SELECT status FROM watchlist WHERE imdbID = :imdbID")
    suspend fun getWatchStatus(imdbID: String): WatchStatus?
}