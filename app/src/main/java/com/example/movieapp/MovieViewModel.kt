package com.example.movieapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import retrofit2.HttpException
import java.io.IOException

class MovieViewModel : ViewModel() {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                Log.d("MovieViewModel", "Searching for movies with query: $query")
                val response = RetrofitInstance.api.searchMovies("a8ae40f2", query)
                if (response.Response == "True") {
                    _movies.value = response.Search
                    Log.d("MovieViewModel", "Found ${response.Search.size} movies")
                } else {
                    _error.value = "No movies found"
                    Log.e("MovieViewModel", "No movies found for query: $query")
                }
            } catch (e: HttpException) {
                _error.value = "Network error: ${e.message()}"
                Log.e("MovieViewModel", "HTTP error", e)
            } catch (e: IOException) {
                _error.value = "Network error: unable to reach server"
                Log.e("MovieViewModel", "IO error", e)
            } catch (e: Exception) {
                _error.value = "An unexpected error occurred: ${e.message}"
                Log.e("MovieViewModel", "Unexpected error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private val _watchlist = MutableStateFlow<Set<String>>(emptySet())
    val watchlist: StateFlow<Set<String>> = _watchlist

    fun toggleWatchlist(movieId: String) {
        val currentWatchlist = _watchlist.value.toMutableSet()
        if (currentWatchlist.contains(movieId)) {
            currentWatchlist.remove(movieId)
        } else {
            currentWatchlist.add(movieId)
        }
        _watchlist.value = currentWatchlist
    }

    fun isInWatchlist(movieId: String): Boolean {
        return _watchlist.value.contains(movieId)
    }
}