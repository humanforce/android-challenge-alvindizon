package com.humanforce.humanforceandroidengineeringchallenge.features.details.model

data class CurrentWeather(
    val temperature: Double,
    val description: String,
    val icon: String,
    val timestamp: Long,
    val feelsLike: Double
)
