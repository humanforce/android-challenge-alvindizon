package com.humanforce.humanforceandroidengineeringchallenge.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.humanforce.humanforceandroidengineeringchallenge.features.details.ui.DetailsScreen
import com.humanforce.humanforceandroidengineeringchallenge.features.home.ui.HomeScreen
import com.humanforce.humanforceandroidengineeringchallenge.features.savedlocations.ui.SavedLocationsScreen
import com.humanforce.humanforceandroidengineeringchallenge.features.search.ui.SearchScreen
import com.humanforce.humanforceandroidengineeringchallenge.navigation.DetailsDestination
import com.humanforce.humanforceandroidengineeringchallenge.navigation.HomeDestination
import com.humanforce.humanforceandroidengineeringchallenge.navigation.SavedLocationsDestination
import com.humanforce.humanforceandroidengineeringchallenge.navigation.SearchDestination
import com.humanforce.humanforceandroidengineeringchallenge.navigation.TopLevelDestination

/**
 * Top-level composable holding the navigation host
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = currentDestination?.hierarchy?.any {
                    it.route?.contains(DetailsDestination::class.qualifiedName.toString()) == true
                } == false
            ) {
                NavigationBar {
                    TopLevelDestination.entries.forEach { section ->
                        val isSelected =
                            currentDestination?.hierarchy?.any {
                                it.route == section.route::class.qualifiedName
                            } == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { navController.navigate(section.route) },
                            icon = { Icon(section.icon, section.label) },
                            label = { Text(section.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = TopLevelDestination.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<HomeDestination> { HomeScreen() }
            composable<SearchDestination> {
                SearchScreen(
                    viewModel = hiltViewModel(),
                    onSearchResultClick = {
                        navController.navigate(
                            route = DetailsDestination(
                                locationName = it.name,
                                latitude = it.lat,
                                longitude = it.lon,
                                country = it.country,
                                state = it.state
                            )
                        )
                    }
                )
            }
            composable<SavedLocationsDestination> {
                SavedLocationsScreen(
                    viewModel = hiltViewModel(),
                    onSavedLocationClick = {
                        navController.navigate(
                            route = DetailsDestination(
                                locationName = it.name,
                                latitude = it.latitude,
                                longitude = it.longitude,
                                country = it.country,
                                state = it.state
                            )
                        )
                    },
                    onNavigationIconClick = { navController.popBackStack() }
                )
            }
            composable<DetailsDestination> {
                DetailsScreen(
                    viewModel = hiltViewModel(),
                    onNavigationIconClick = { navController.popBackStack() })
            }
        }
    }
}