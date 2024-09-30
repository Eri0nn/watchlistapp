package com.example.movieapp


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WatchlistScreen(viewModel: MovieViewModel = viewModel()) {
    val watchlist by viewModel.watchlist.collectAsState()
    val movies by viewModel.movies.collectAsState()

    val watchlistMovies = movies.filter { viewModel.isInWatchlist(it.imdbID) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (watchlistMovies.isNotEmpty()) {
            LazyColumn {
                items(watchlistMovies) { movie ->
                    MovieItem(movie, viewModel)
                }
            }
        } else {
            Text("No movies in watchlist", modifier = Modifier.padding(16.dp))
        }
    }
}