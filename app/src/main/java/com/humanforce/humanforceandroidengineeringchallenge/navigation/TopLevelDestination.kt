package com.humanforce.humanforceandroidengineeringchallenge.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    Home(
        route = "home",
        icon = Icons.Default.Home,
        label = "Home"
    ),
    SavedLocations(
        route = "saved_locations",
        icon = Icons.Default.Bookmarks,
        label = "Locations"
    ),
    Search(
        route = "search",
        icon = Icons.Default.Search,
        label = "Search"
    );
}