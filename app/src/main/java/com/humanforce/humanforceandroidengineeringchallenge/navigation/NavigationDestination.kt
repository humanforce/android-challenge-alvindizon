package com.humanforce.humanforceandroidengineeringchallenge.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class NavigationDestination

@Serializable
data object HomeDestination: NavigationDestination()

@Serializable
data object SavedLocationsDestination: NavigationDestination()

@Serializable
data object SearchDestination: NavigationDestination()

@Serializable
data class DetailsDestination(
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val state: String
): NavigationDestination()
