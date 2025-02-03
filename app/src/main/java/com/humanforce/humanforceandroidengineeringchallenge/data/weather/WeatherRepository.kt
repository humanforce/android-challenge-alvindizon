package com.humanforce.humanforceandroidengineeringchallenge.data.weather

import com.humanforce.humanforceandroidengineeringchallenge.data.remote.OpenWeatherApi
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.mapper.toCurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.mapper.toWeatherForecastData
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.CurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.WeatherForecastData
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherRepository {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        unit: String
    ): CurrentWeatherData

    suspend fun getForecasts(
        latitude: Double,
        longitude: Double,
        unit: String
    ): List<WeatherForecastData>
}

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val openWeatherApi: OpenWeatherApi
) :
    WeatherRepository {

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        unit: String
    ): CurrentWeatherData {
        return openWeatherApi.getCurrentWeather(
            lat = latitude,
            lon = longitude,
            units = unit
        )
            .toCurrentWeatherData()
    }

    override suspend fun getForecasts(
        latitude: Double,
        longitude: Double,
        unit: String
    ): List<WeatherForecastData> {
        return openWeatherApi.getForecast(
            lat = latitude,
            lon = longitude,
            units = unit
        ).list?.map {
            it.toWeatherForecastData()
        } ?: emptyList()
    }
}