package com.humanforce.humanforceandroidengineeringchallenge.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetForecastResponse(
    val clouds: Clouds? = null,
    val dt: Long? = null,
    val main: Main? = null,
    val pop: Double? = null,
    val weather: List<Weather>? = null,
    val wind: Wind? = null
)

@Serializable
data class Main(
    @SerialName("feels_like")
    val feelsLike: Double?,
    val humidity: Int?,
    val pressure: Int?,
    val temp: Double?,
    @SerialName("temp_max")
    val tempMax: Double?,
    @SerialName("temp_min")
    val tempMin: Double?
)

@Serializable
data class Weather(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val main: String?
)

@Serializable
data class Wind(
    val deg: Int?,
    val gust: Double?,
    val speed: Double?
)
@Serializable
data class Clouds(
    val all: Int?
)
