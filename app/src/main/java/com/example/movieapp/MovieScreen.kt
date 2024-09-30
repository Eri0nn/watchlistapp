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

@Composable
fun MovieScreen(viewModel: MovieViewModel = viewModel()) {
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Watchlist App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.searchMovies(searchQuery) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            error != null -> {
                Text(
                    text = error ?: "Unknown error occurred",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            movies.isNotEmpty() -> {
                LazyColumn {
                    items(movies) { movie ->
                        MovieItem(movie, viewModel)
                    }
                }
            }
            else -> {
                Text(
                    "No movies found",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, viewModel: MovieViewModel) {
    var isInWatchlist by remember { mutableStateOf(false) }

    LaunchedEffect(movie.imdbID) {
        isInWatchlist = viewModel.isInWatchlist(movie.imdbID)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = movie.Poster,
                contentDescription = "${movie.Title} poster",
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 16.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.Title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Year: ${movie.Year}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Type: ${movie.Type}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = {
                viewModel.toggleWatchlist(movie)
                isInWatchlist = !isInWatchlist
            }) {
                Icon(
                    imageVector = if (isInWatchlist) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = "Toggle Watchlist",
                    tint = if (isInWatchlist) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
