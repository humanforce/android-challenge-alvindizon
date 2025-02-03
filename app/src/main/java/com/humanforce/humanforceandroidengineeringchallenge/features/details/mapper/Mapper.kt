package com.humanforce.humanforceandroidengineeringchallenge.features.details.mapper

import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.WeatherForecastData
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.WeatherForecast

fun WeatherForecastData.toWeatherForecast(): WeatherForecast {
    return WeatherForecast(
        cloudiness = cloudiness,
        feelsLike = feelsLike,
        humidity = humidity,
        pressure = pressure,
        temperature = temperature,
        maxTemperature = maxTemperature,
        minTemperature = minTemperature,
        timestamp = timestamp,
        precipitationProbability = precipitationProbability,
        windDirectionDegrees = windDirectionDegrees,
        windSpeed = windSpeed,
        windGust = windGust,
        description = description,
        icon = icon
    )
}