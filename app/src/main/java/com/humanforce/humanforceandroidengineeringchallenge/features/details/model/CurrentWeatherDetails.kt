package com.humanforce.humanforceandroidengineeringchallenge.features.details.model

data class CurrentWeatherDetails(
    val temperatureString: String,
    val description: String,
    val icon: String,
    val timestamp: Long,
    val feelsLikeString: String
)
