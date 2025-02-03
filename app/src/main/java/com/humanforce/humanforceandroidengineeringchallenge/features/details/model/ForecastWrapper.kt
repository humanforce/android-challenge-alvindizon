package com.humanforce.humanforceandroidengineeringchallenge.features.details.model

data class ForecastWrapper(
    val currentWeather: CurrentWeatherDetails,
    val aggregatedDailyForecasts: List<AggregateDailyForecast>
)
