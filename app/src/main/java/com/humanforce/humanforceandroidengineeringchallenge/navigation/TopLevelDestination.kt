package com.humanforce.humanforceandroidengineeringchallenge.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents bottom navigation destinations.
 */
enum class TopLevelDestination(
    val route: NavigationDestination,
    val icon: ImageVector,
    val label: String
) {
    Home(
        route = HomeDestination,
        icon = Icons.Default.Home,
        label = "Home"
    ),
    SavedLocations(
        route = SavedLocationsDestination,
        icon = Icons.Default.Bookmarks,
        label = "Locations"
    ),
    Search(
        route = SearchDestination,
        icon = Icons.Default.Search,
        label = "Search"
    );
}