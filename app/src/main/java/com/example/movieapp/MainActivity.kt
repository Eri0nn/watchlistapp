package com.example.movieapp

import MovieDetailScreen
import WatchlistScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.movieapp.ui.theme.MovieAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val items = listOf(
                    BottomNavItem("search", "Search", Icons.Default.Search),
                    BottomNavItem("watchlist", "Watchlist", Icons.Default.Favorite)
                )

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "search",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("search") { MovieScreen(navController = navController) }
            composable("watchlist") { WatchlistScreen(navController = navController) }
            composable("movieDetail/{imdbID}") { backStackEntry ->
                val imdbID = backStackEntry.arguments?.getString("imdbID") ?: return@composable
                MovieDetailScreen(imdbID = imdbID, onBackPressed = { navController.popBackStack() })
            }
        }
    }
}

data class BottomNavItem(val route: String, val title: String, val icon: ImageVector)

@Composable
fun WatchlistScreen(navController: NavController) {
    var watchlist by remember { mutableStateOf(listOf<String>()) }

    Column {
        Text("Watchlist", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
        LazyColumn {
            items(watchlist) { movie ->
                WatchlistItem(
                    movie = movie,
                    onDelete = {
                        watchlist = watchlist.filter { it != movie }
                    }
                )
            }
        }
    }
}

@Composable
fun WatchlistItem(movie: String, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(movie, style = MaterialTheme.typography.bodyLarge)
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}