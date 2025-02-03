package com.humanforce.humanforceandroidengineeringchallenge.data.weather.mapper

import com.humanforce.humanforceandroidengineeringchallenge.data.remote.response.GetCurrentWeatherResponse
import com.humanforce.humanforceandroidengineeringchallenge.data.remote.response.GetForecastResponse.GetForecastResponseItem
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.CurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.WeatherForecastData
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.WeatherForecast

fun GetCurrentWeatherResponse.toCurrentWeatherData(): CurrentWeatherData {
    return CurrentWeatherData(
        temperature = main?.temp ?: 0.0,
        description = weather?.firstOrNull()?.description.orEmpty(),
        icon = weather?.firstOrNull()?.icon.orEmpty(),
        timestamp = dt ?: 0L,
        feelsLike = main?.feelsLike ?: 0.0,
    )
}

fun GetForecastResponseItem.toWeatherForecastData(): WeatherForecastData {
    return WeatherForecastData(
        cloudiness = clouds?.all?.toDouble() ?: 0.0,
        feelsLike = main?.feelsLike ?: 0.0,
        humidity = main?.humidity ?: 0,
        pressure = main?.pressure ?: 0,
        temperature = main?.temp ?: 0.0,
        maxTemperature = main?.tempMax ?: 0.0,
        minTemperature = main?.tempMin ?: 0.0,
        timestamp = dt ?: 0L,
        precipitationProbability = pop ?: 0.0,
        windDirectionDegrees = wind?.deg ?: 0,
        windSpeed = wind?.speed ?: 0.0,
        windGust = wind?.gust ?: 0.0,
        description = weather?.firstOrNull()?.description.orEmpty(),
        icon = weather?.firstOrNull()?.icon.orEmpty()
    )
}