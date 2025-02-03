package com.humanforce.humanforceandroidengineeringchallenge.features.home.mapper

import com.humanforce.humanforceandroidengineeringchallenge.common.units.MeasurementUnit
import com.humanforce.humanforceandroidengineeringchallenge.common.units.toTemperatureString
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.CurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.CurrentWeatherDetails
import com.humanforce.humanforceandroidengineeringchallenge.features.home.model.CurrentLocationWeather

fun CurrentWeatherData.toCurrentLocationWeather(measurementUnit: MeasurementUnit): CurrentLocationWeather {
    return CurrentLocationWeather(
        temperatureString = temperature.toTemperatureString(measurementUnit),
        description = description,
        icon = icon,
        timestamp = timestamp,
        feelsLikeString = feelsLike.toTemperatureString(measurementUnit)
    )
}