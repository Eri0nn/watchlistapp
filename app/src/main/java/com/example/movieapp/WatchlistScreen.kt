import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieapp.data.WatchlistItem
import com.example.movieapp.data.WatchStatus
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieapp.Movie
import com.example.movieapp.MovieViewModel

enum class WatchlistFilter {
    ALL, PLANNED, COMPLETED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(viewModel: MovieViewModel = viewModel()) {
    var currentFilter by remember { mutableStateOf(WatchlistFilter.ALL) }
    val watchlist by viewModel.watchlist.collectAsState(initial = emptyList())

    val filteredWatchlist = when (currentFilter) {
        WatchlistFilter.ALL -> watchlist
        WatchlistFilter.PLANNED -> watchlist.filter { it.status == WatchStatus.PLANNED }
        WatchlistFilter.COMPLETED -> watchlist.filter { it.status == WatchStatus.COMPLETED }
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterSelector(currentFilter) { newFilter ->
                currentFilter = newFilter
            }

            if (filteredWatchlist.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredWatchlist) { watchlistItem ->
                        WatchlistItemCard(watchlistItem, viewModel)
                    }
                }
            } else {
                EmptyWatchlistMessage(currentFilter)
            }
        }
    }
}

@Composable
fun FilterSelector(currentFilter: WatchlistFilter, onFilterSelected: (WatchlistFilter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterChip(
            selected = currentFilter == WatchlistFilter.ALL,
            onClick = { onFilterSelected(WatchlistFilter.ALL) },
            label = { Text("All") }
        )
        FilterChip(
            selected = currentFilter == WatchlistFilter.PLANNED,
            onClick = { onFilterSelected(WatchlistFilter.PLANNED) },
            label = { Text("Planned") }
        )
        FilterChip(
            selected = currentFilter == WatchlistFilter.COMPLETED,
            onClick = { onFilterSelected(WatchlistFilter.COMPLETED) },
            label = { Text("Completed") }
        )
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
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f)
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
                    WatchStatusToggle(watchlistItem, viewModel)
                }
            }
            IconButton(
                onClick = {
                    viewModel.toggleWatchlist(
                        Movie(
                            imdbID = watchlistItem.imdbID,
                            Title = watchlistItem.title,
                            Year = watchlistItem.year,
                            Poster = watchlistItem.poster,
                                Type = "movie",
                            Plot = "",
                            Director = "",
                            Actors = "",
                            Genre = "",
                            Rated = "",
                            Released = "",
                            Runtime = "",
                            Writer = "",
                            Language = "",
                            Country = "",
                            Awards = "",
                            Ratings = emptyList(),
                            Metascore = "",
                            imdbRating = "",
                            imdbVotes = "",
                            DVD = "",
                            BoxOffice = "",
                            Production = "",
                            Website = "",
                            Response = ""



                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove from Watchlist",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun WatchStatusToggle(watchlistItem: WatchlistItem, viewModel: MovieViewModel) {
    var status by remember { mutableStateOf(watchlistItem.status) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Status:",
            style = MaterialTheme.typography.bodyMedium
        )
        Switch(
            checked = status == WatchStatus.COMPLETED,
            onCheckedChange = { isCompleted ->
                val newStatus = if (isCompleted) WatchStatus.COMPLETED else WatchStatus.PLANNED
                status = newStatus
                viewModel.updateWatchStatus(watchlistItem.imdbID, newStatus)
            },
            thumbContent = {
                Icon(
                    imageVector = if (status == WatchStatus.COMPLETED) Icons.Default.Check else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        )
    }
}

@Composable
fun EmptyWatchlistMessage(filter: WatchlistFilter) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (filter) {
                WatchlistFilter.ALL -> "Your watchlist is empty"
                WatchlistFilter.PLANNED -> "No planned items in your watchlist"
                WatchlistFilter.COMPLETED -> "No completed items in your watchlist"
            },
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}