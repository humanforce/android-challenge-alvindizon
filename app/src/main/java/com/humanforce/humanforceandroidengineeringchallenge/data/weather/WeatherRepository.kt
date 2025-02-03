package com.humanforce.humanforceandroidengineeringchallenge.data.weather

import com.humanforce.humanforceandroidengineeringchallenge.data.remote.OpenWeatherApi
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.mapper.toCurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.mapper.toWeatherForecastData
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.CurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.WeatherForecastData
import javax.inject.Inject
import javax.inject.Singleton

interface WeatherRepository {
    suspend fun getCurrentWeather(latitude: Double, longitude: Double): CurrentWeatherData

    suspend fun getForecasts(latitude: Double, longitude: Double): List<WeatherForecastData>
}

@Singleton
class WeatherRepositoryImpl @Inject constructor(private val openWeatherApi: OpenWeatherApi) :
    WeatherRepository {

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): CurrentWeatherData {
        return openWeatherApi.getCurrentWeather(lat = latitude, lon = longitude)
            .toCurrentWeatherData()
    }

    override suspend fun getForecasts(latitude: Double, longitude: Double): List<WeatherForecastData> {
        return openWeatherApi.getForecast(
            lat = latitude,
            lon = longitude
        ).list?.map {
            it.toWeatherForecastData()
        } ?: emptyList()
    }
}