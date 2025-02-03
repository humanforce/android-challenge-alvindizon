package com.humanforce.humanforceandroidengineeringchallenge.features.details.model

import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.CurrentWeatherData

data class ForecastWrapper(
    val currentWeather: CurrentWeatherData,
    val aggregatedDailyForecasts: List<AggregateDailyForecast>
)
