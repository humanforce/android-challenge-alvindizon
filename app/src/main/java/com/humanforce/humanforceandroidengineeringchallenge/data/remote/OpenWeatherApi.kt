package com.humanforce.humanforceandroidengineeringchallenge.data.remote

import com.humanforce.humanforceandroidengineeringchallenge.data.remote.response.GetCitiesResponseItem
import com.humanforce.humanforceandroidengineeringchallenge.data.remote.response.GetForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

private const val UNIT_METRIC = "metric"

interface OpenWeatherApi {
    @GET("geo/1.0/direct")
    suspend fun getCities(
        @Query("q") query: String,
        @Query("limit") limit: String,
        @Query("appId") appId: String = APIKeyManager.apiKey
    ): List<GetCitiesResponseItem>

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = UNIT_METRIC,
        @Query("appId") appId: String = APIKeyManager.apiKey
    ): GetForecastResponse
}