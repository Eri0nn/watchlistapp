package com.example.movieapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder

@Composable
fun MovieScreen(viewModel: MovieViewModel = viewModel()) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Button(
            onClick = { viewModel.searchMovies(searchQuery) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Search")
        }

        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            error != null -> {
                Text(error ?: "Unknown error occurred", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }
            movies.isNotEmpty() -> {
                LazyColumn {
                    items(movies) { movie ->
                        MovieItem(movie, viewModel)
                    }
                }
            }
            else -> {
                Text("No movies found", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, viewModel: MovieViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = movie.Poster,
                contentDescription = "${movie.Title} poster",
                modifier = Modifier.size(100.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(movie.Title, style = MaterialTheme.typography.headlineSmall)
                Text(movie.Year, style = MaterialTheme.typography.bodyMedium)
                Text(movie.Type, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { viewModel.toggleWatchlist(movie.imdbID) }) {
                Icon(
                    imageVector = if (viewModel.isInWatchlist(movie.imdbID)) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = "Add to Watchlist",
                    tint = if (viewModel.isInWatchlist(movie.imdbID)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}