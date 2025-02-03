package com.humanforce.humanforceandroidengineeringchallenge.features.details.usecase

import com.humanforce.humanforceandroidengineeringchallenge.common.threading.AppCoroutineDispatchers
import com.humanforce.humanforceandroidengineeringchallenge.common.units.MeasurementUnit
import com.humanforce.humanforceandroidengineeringchallenge.common.units.toTemperatureString
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.WeatherRepository
import com.humanforce.humanforceandroidengineeringchallenge.features.details.mapper.toCurrentWeatherDetails
import com.humanforce.humanforceandroidengineeringchallenge.features.details.mapper.toWeatherForecast
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.AggregateDailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.CurrentWeatherDetails
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

    suspend fun invoke(lat: Double, lon: Double, unit: MeasurementUnit): ForecastWrapper

}

@Singleton
class GetForecastDetailsUseCaseImpl @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val weatherRepository: WeatherRepository,
) : GetForecastDetailsUseCase {

    override suspend fun invoke(lat: Double, lon: Double, unit: MeasurementUnit): ForecastWrapper {
        return withContext(appCoroutineDispatchers.io) {
            val forecasts = async { loadForecasts(lat, lon, unit) }
            val currentWeather = async { loadCurrentWeather(lat, lon, unit) }
            ForecastWrapper(currentWeather.await(), forecasts.await())
        }
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    private suspend fun loadForecasts(
        latitude: Double,
        longitude: Double,
        unit: MeasurementUnit
    ): List<AggregateDailyForecast> {
        val forecasts = weatherRepository.getForecasts(
            latitude = latitude,
            longitude = longitude,
            unit = unit.name
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
                    .toTemperatureString(unit),
                maxTemperatureString = forecasts.maxOf { it.temperature }
                    .toTemperatureString(unit)
            )
        }
    }

    private suspend fun loadCurrentWeather(latitude: Double, longitude: Double, unit: MeasurementUnit): CurrentWeatherDetails {
        return weatherRepository.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            unit = unit.name
        ).toCurrentWeatherDetails(measurementUnit = unit)
    }
}