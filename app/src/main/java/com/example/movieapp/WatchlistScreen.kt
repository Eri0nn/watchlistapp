package com.example.movieapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import com.example.movieapp.data.WatchlistItem  // Add this import

@Composable
fun WatchlistScreen(viewModel: MovieViewModel = viewModel()) {
    val watchlist by viewModel.watchlist.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        if (watchlist.isNotEmpty()) {
            LazyColumn {
                items(watchlist) { watchlistItem ->
                    WatchlistItemCard(watchlistItem, viewModel)
                }
            }
        } else {
            Text("No movies in watchlist", modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun WatchlistItemCard(watchlistItem: WatchlistItem, viewModel: MovieViewModel) {
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
                model = watchlistItem.poster,
                contentDescription = "${watchlistItem.title} poster",
                modifier = Modifier.size(100.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(watchlistItem.title, style = MaterialTheme.typography.headlineSmall)
                Text(watchlistItem.year, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = {
                viewModel.toggleWatchlist(
                    Movie(
                        Title = watchlistItem.title,
                        Year = watchlistItem.year,
                        Rated = "", // Provide appropriate value
                        Released = "", // Provide appropriate value
                        Runtime = "", // Provide appropriate value
                        Genre = "", // Provide appropriate value
                        Director = "", // Provide appropriate value
                        Writer = "", // Provide appropriate value
                        Actors = "", // Provide appropriate value
                        Plot = "", // Provide appropriate value
                        Language = "", // Provide appropriate value
                        Country = "", // Provide appropriate value
                        Awards = "", // Provide appropriate value
                        Poster = watchlistItem.poster,
                        Ratings = emptyList(), // Provide appropriate value
                        Metascore = "", // Provide appropriate value
                        imdbRating = "", // Provide appropriate value
                        imdbVotes = "", // Provide appropriate value
                        imdbID = watchlistItem.imdbID,
                        Type = "movie",
                        DVD = "", // Provide appropriate value
                        BoxOffice = "", // Provide appropriate value
                        Production = "", // Provide appropriate value
                        Website = "", // Provide appropriate value
                        Response = "" // Provide appropriate value
                    )
                )
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from Watchlist",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}