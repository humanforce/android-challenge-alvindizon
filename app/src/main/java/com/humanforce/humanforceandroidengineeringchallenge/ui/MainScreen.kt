package com.humanforce.humanforceandroidengineeringchallenge.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.humanforce.humanforceandroidengineeringchallenge.features.home.ui.HomeScreen
import com.humanforce.humanforceandroidengineeringchallenge.features.savedlocations.ui.SavedLocationsScreen
import com.humanforce.humanforceandroidengineeringchallenge.features.search.ui.SearchScreen
import com.humanforce.humanforceandroidengineeringchallenge.navigation.TopLevelDestination

/**
 * Top-level composable holding the navigation host
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                TopLevelDestination.entries.forEach { section ->
                    NavigationBarItem(
                        selected = currentRoute == section.route,
                        onClick = { navController.navigate(section.route) },
                        icon = { Icon(section.icon, section.label) },
                        label = { Text(section.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = TopLevelDestination.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(TopLevelDestination.Home.route) { HomeScreen() }
            composable(TopLevelDestination.Search.route) {
                SearchScreen(
                    viewModel = hiltViewModel(),
                    onSearchResultClick = {})
            }
            composable(TopLevelDestination.SavedLocations.route) { SavedLocationsScreen() }
        }
    }
}