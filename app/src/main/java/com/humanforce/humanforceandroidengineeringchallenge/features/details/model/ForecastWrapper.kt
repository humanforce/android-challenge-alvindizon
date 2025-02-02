package com.humanforce.humanforceandroidengineeringchallenge.features.details.model

data class ForecastWrapper(
    val currentWeather: CurrentWeather,
    val aggregatedDailyForecasts: List<AggregateDailyForecast>
)
