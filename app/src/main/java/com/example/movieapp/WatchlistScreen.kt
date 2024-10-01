import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieapp.Movie
import com.example.movieapp.MovieViewModel
import com.example.movieapp.data.WatchlistItem
import com.example.movieapp.ui.theme.MovieAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(viewModel: MovieViewModel = viewModel()) {
    val watchlist by viewModel.watchlist.collectAsState(initial = emptyList())

    MovieAppTheme {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("My Watchlist") },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                if (watchlist.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(watchlist) { watchlistItem ->
                            WatchlistItemCard(watchlistItem, viewModel)
                        }
                    }
                } else {
                    EmptyWatchlistMessage()
                }
            }
        }
    }
}

@Composable
fun WatchlistItemCard(watchlistItem: WatchlistItem, viewModel: MovieViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            AsyncImage(
                model = watchlistItem.poster,
                contentDescription = "${watchlistItem.title} poster",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f) // Increased opacity
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = watchlistItem.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = watchlistItem.year,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            IconButton(
                onClick = {
                    viewModel.toggleWatchlist(
                        Movie(
                            Title = watchlistItem.title,
                            Year = watchlistItem.year,
                            Poster = watchlistItem.poster,
                            imdbID = watchlistItem.imdbID,
                            Type = "movie",
                            Actors = "", // Add appropriate value
                            Awards = "", // Add appropriate value
                            BoxOffice = "", // Add appropriate value
                            Country = "", // Add appropriate value
                            DVD = "", // Add appropriate value
                            Director = "", // Add appropriate value
                            Genre = "", // Add appropriate value
                            Language = "", // Add appropriate value
                            Metascore = "", // Add appropriate value
                            Plot = "", // Add appropriate value
                            Production = "", // Add appropriate value
                            Rated = "", // Add appropriate value
                            Ratings = emptyList(), // Add appropriate value
                            Released = "", // Add appropriate value
                            Response = "", // Add appropriate value
                            Runtime = "", // Add appropriate value
                            Website = "", // Add appropriate value
                            Writer = "", // Add appropriate value
                            imdbRating = "", // Add appropriate value
                            imdbVotes = "" // Add appropriate value
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), shape = MaterialTheme.shapes.small) // Added background to icon button
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from Watchlist",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp) // Increased icon size
                )
            }
        }
    }
}

@Composable
fun EmptyWatchlistMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Your watchlist is empty",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}