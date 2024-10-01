package com.example.movieapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.WatchlistItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.navigation.NavController
import com.example.movieapp.data.WatchStatus
import retrofit2.HttpException
import java.io.IOException

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val watchlistDao = database.watchlistDao()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val watchlist = watchlistDao.getAllWatchlistItems()

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = RetrofitInstance.api.searchMovies("a8ae40f2", query)
                if (response.Response == "True") {
                    _movies.value = response.Search
                } else {
                    _error.value = "No movies found"
                }
            } catch (e: HttpException) {
                _error.value = "Network error: ${e.message()}"
            } catch (e: IOException) {
                _error.value = "Network error: unable to reach server"
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun toggleWatchlist(movie: Movie) {
        viewModelScope.launch {
            val watchlistItem = WatchlistItem(movie.imdbID, movie.Title, movie.Year, movie.Poster)
            if (watchlistDao.isInWatchlist(movie.imdbID)) {
                watchlistDao.deleteWatchlistItem(watchlistItem)
            } else {
                watchlistDao.insertWatchlistItem(watchlistItem)
            }
        }
    }

    fun updateWatchStatus(imdbID: String, status: WatchStatus) {
        viewModelScope.launch {
            watchlistDao.updateWatchStatus(imdbID, status)
        }
    }

    suspend fun getWatchStatus(imdbID: String): WatchStatus? {
        return watchlistDao.getWatchStatus(imdbID)
    }

    suspend fun isInWatchlist(movieId: String): Boolean {
        return watchlistDao.isInWatchlist(movieId)
    }

    fun onMovieClicked(navController: NavController, movie: Movie) {
        navController.navigate("movieDetail/${movie.imdbID}")
    }



}