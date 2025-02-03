package com.humanforce.humanforceandroidengineeringchallenge.features.details.mapper

import com.humanforce.humanforceandroidengineeringchallenge.common.units.MeasurementUnit
import com.humanforce.humanforceandroidengineeringchallenge.common.units.toTemperatureString
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.CurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.WeatherForecastData
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.CurrentWeatherDetails
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

fun CurrentWeatherData.toCurrentWeatherDetails(measurementUnit: MeasurementUnit): CurrentWeatherDetails {
    return CurrentWeatherDetails(
        temperatureString = temperature.toTemperatureString(measurementUnit),
        description = description,
        icon = icon,
        timestamp = timestamp,
        feelsLikeString = feelsLike.toTemperatureString(measurementUnit)
    )
}