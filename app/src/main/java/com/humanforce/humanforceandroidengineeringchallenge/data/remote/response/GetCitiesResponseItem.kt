package com.humanforce.humanforceandroidengineeringchallenge.data.remote.response


import kotlinx.serialization.Serializable

@Serializable
data class GetCitiesResponseItem(
    val country: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val name: String? = null,
    val state: String? = null,
)