import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieapp.Movie
import com.example.movieapp.data.WatchlistItem
import com.example.movieapp.data.WatchStatus
import com.example.movieapp.MovieViewModel

enum class WatchlistFilter {
    ALL, PLANNED, COMPLETED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(viewModel: MovieViewModel = viewModel()) {
    var currentFilter by remember { mutableStateOf(WatchlistFilter.ALL) }
    var currentSort by remember { mutableStateOf("title") }
    val watchlist by viewModel.watchlist.collectAsState(initial = emptyList())

    val filteredWatchlist = when (currentFilter) {
        WatchlistFilter.ALL -> watchlist
        WatchlistFilter.PLANNED -> watchlist.filter { it.status == WatchStatus.PLANNED }
        WatchlistFilter.COMPLETED -> watchlist.filter { it.status == WatchStatus.COMPLETED }
    }.let { list ->
        when (currentSort) {
            "title" -> list.sortedBy { it.title }
            "year" -> list.sortedByDescending { it.year }
            else -> list
        }
    }

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterAndSortBar(
                currentFilter = currentFilter,
                currentSort = currentSort,
                onFilterChange = { currentFilter = it },
                onSortChange = { currentSort = it }
            )

            if (filteredWatchlist.isEmpty()) {
                EmptyWatchlistMessage(currentFilter)
            } else {
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
            }
        }
    }
}

@Composable
fun FilterAndSortBar(
    currentFilter: WatchlistFilter,
    currentSort: String,
    onFilterChange: (WatchlistFilter) -> Unit,
    onSortChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterDropdown(currentFilter, onFilterChange)
        SortDropdown(currentSort, onSortChange)
    }
}

@Composable
fun FilterDropdown(currentFilter: WatchlistFilter, onFilterChange: (WatchlistFilter) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { expanded = true }) {
            Text("Filter: ${currentFilter.name.capitalize()}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            WatchlistFilter.values().forEach { filter ->
                DropdownMenuItem(
                    text = { Text(filter.name.capitalize()) },
                    onClick = {
                        onFilterChange(filter)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SortDropdown(currentSort: String, onSortChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { expanded = true }) {
            Text("Sort: ${currentSort.capitalize()}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("title", "year", "rating", "release date").forEach { sortOption ->
                DropdownMenuItem(
                    text = { Text("Sort by ${sortOption.capitalize()}") },
                    onClick = {
                        onSortChange(sortOption)
                        expanded = false
                    },
                    leadingIcon = {
                        if (currentSort == sortOption) Icon(Icons.Default.Check, contentDescription = null)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistItemCard(watchlistItem: WatchlistItem, viewModel: MovieViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { expanded = !expanded }
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
                    if (expanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        WatchStatusToggle(watchlistItem, viewModel)
                    }
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
                            imdbRating = "",
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
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
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
}