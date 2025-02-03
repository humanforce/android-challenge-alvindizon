package com.humanforce.humanforceandroidengineeringchallenge.features.search.model

data class SearchResult(
    val name: String,
    val state: String,
    val country: String,
    val lat: Double,
    val lon: Double
)
