package com.humanforce.humanforceandroidengineeringchallenge.features.details.usecase

import com.humanforce.humanforceandroidengineeringchallenge.common.threading.AppCoroutineDispatchers
import com.humanforce.humanforceandroidengineeringchallenge.common.units.Temperature
import com.humanforce.humanforceandroidengineeringchallenge.common.units.toTemperatureString
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.WeatherRepository
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.CurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.features.details.mapper.toWeatherForecast
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.AggregateDailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.ForecastWrapper
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

interface GetForecastDetailsUseCase {

    suspend fun invoke(lat: Double, lon: Double): ForecastWrapper

}

@Singleton
class GetForecastDetailsUseCaseImpl @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val weatherRepository: WeatherRepository
) : GetForecastDetailsUseCase {

    override suspend fun invoke(lat: Double, lon: Double): ForecastWrapper {
        return withContext(appCoroutineDispatchers.io) {
            val forecasts = async { loadForecasts(lat, lon) }
            val currentWeather = async { loadCurrentWeather(lat, lon) }
            ForecastWrapper(currentWeather.await(), forecasts.await())
        }
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    private suspend fun loadForecasts(
        latitude: Double,
        longitude: Double
    ): List<AggregateDailyForecast> {
        val forecasts = weatherRepository.getForecasts(
            latitude = latitude,
            longitude = longitude
        ).map { it.toWeatherForecast() }
        // Group by date, then get lowest temp and highest temperature for each date
        return forecasts.groupBy { forecast ->
            Instant.fromEpochSeconds(forecast.timestamp)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        }.map { (date, forecasts) ->
            AggregateDailyForecast(
                formattedDate = LocalDate.Format { byUnicodePattern("yyyy-MM-dd") }
                    .format(date),
                icon = forecasts.firstOrNull { it.icon.contains("d") }?.icon
                    ?: forecasts.firstOrNull()?.icon.orEmpty(),
                minTemperatureString = forecasts.minOf { it.temperature }
                    .toTemperatureString(Temperature.Celsius),
                maxTemperatureString = forecasts.maxOf { it.temperature }
                    .toTemperatureString(Temperature.Celsius)
            )
        }
    }

    private suspend fun loadCurrentWeather(latitude: Double, longitude: Double): CurrentWeatherData {
        return weatherRepository.getCurrentWeather(
            latitude = latitude,
            longitude = longitude
        )
    }
}