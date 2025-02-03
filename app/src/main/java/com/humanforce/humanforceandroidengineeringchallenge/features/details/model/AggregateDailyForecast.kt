package com.humanforce.humanforceandroidengineeringchallenge.features.details.model

data class AggregateDailyForecast(
    val formattedDate: String,
    val icon: String,
    val minTemperatureString: String,
    val maxTemperatureString: String
)
