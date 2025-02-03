package com.humanforce.humanforceandroidengineeringchallenge.data.weather.model

data class WeatherForecastData(
    val cloudiness: Double,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val timestamp: Long,
    val precipitationProbability: Double,
    val windDirectionDegrees: Int,
    val windSpeed: Double,
    val windGust: Double,
    val description: String,
    val icon: String
)
