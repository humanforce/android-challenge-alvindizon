package com.humanforce.humanforceandroidengineeringchallenge.data.weather.model

data class CurrentWeatherData(
    val temperature: Double,
    val description: String,
    val icon: String,
    val timestamp: Long,
    val feelsLike: Double
)
