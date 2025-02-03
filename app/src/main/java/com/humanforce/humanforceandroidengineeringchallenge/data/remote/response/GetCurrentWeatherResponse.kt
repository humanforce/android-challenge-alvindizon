package com.humanforce.humanforceandroidengineeringchallenge.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCurrentWeatherResponse(
    val clouds: Clouds? = null,
    val dt: Long? = null,
    val main: Main? = null,
    val sys: Sys? = null,
    val weather: List<Weather?>? = null,
    val wind: Wind? = null,
    val state: String? = null
) {
    @Serializable
    data class Clouds(
        val all: Int? = null
    )

    @Serializable
    data class Main(
        @SerialName("feels_like")
        val feelsLike: Double? = null,
        @SerialName("grnd_level")
        val grndLevel: Int? = null,
        val humidity: Int? = null,
        val pressure: Int? = null,
        @SerialName("sea_level")
        val seaLevel: Int? = null,
        val temp: Double? = null,
        @SerialName("temp_max")
        val tempMax: Double? = null,
        @SerialName("temp_min")
        val tempMin: Double? = null
    )

    @Serializable
    data class Sys(
        val country: String? = null,
        val sunrise: Int? = null,
        val sunset: Int? = null
    )

    @Serializable
    data class Weather(
        val description: String? = null,
        val icon: String? = null,
        val id: Int? = null,
        val main: String? = null
    )

    @Serializable
    data class Wind(
        val deg: Int? = null,
        val gust: Double? = null,
        val speed: Double? = null
    )
}