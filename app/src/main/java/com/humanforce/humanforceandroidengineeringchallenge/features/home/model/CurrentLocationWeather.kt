package com.humanforce.humanforceandroidengineeringchallenge.features.home.model

data class CurrentLocationWeather(
    val temperatureString: String,
    val description: String,
    val icon: String,
    val timestamp: Long,
    val feelsLikeString: String
)
