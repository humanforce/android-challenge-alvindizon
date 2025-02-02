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
    val wind: Wind? = null
) {
    @Serializable
    data class Clouds(
        @SerialName("all")
        val all: Int? = null
    )

    @Serializable
    data class Main(
        @SerialName("feels_like")
        val feelsLike: Double? = null,
        @SerialName("grnd_level")
        val grndLevel: Int? = null,
        @SerialName("humidity")
        val humidity: Int? = null,
        @SerialName("pressure")
        val pressure: Int? = null,
        @SerialName("sea_level")
        val seaLevel: Int? = null,
        @SerialName("temp")
        val temp: Double? = null,
        @SerialName("temp_max")
        val tempMax: Double? = null,
        @SerialName("temp_min")
        val tempMin: Double? = null
    )

    @Serializable
    data class Sys(
        @SerialName("country")
        val country: String? = null,
        @SerialName("sunrise")
        val sunrise: Int? = null,
        @SerialName("sunset")
        val sunset: Int? = null
    )

    @Serializable
    data class Weather(
        @SerialName("description")
        val description: String? = null,
        @SerialName("icon")
        val icon: String? = null,
        @SerialName("id")
        val id: Int? = null,
        @SerialName("main")
        val main: String? = null
    )

    @Serializable
    data class Wind(
        @SerialName("deg")
        val deg: Int? = null,
        @SerialName("gust")
        val gust: Double? = null,
        @SerialName("speed")
        val speed: Double? = null
    )
}