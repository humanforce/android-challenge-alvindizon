package com.humanforce.humanforceandroidengineeringchallenge.data.remote.response


import kotlinx.serialization.Serializable

@Serializable
data class GetCitiesResponseItem(
    val country: String?,
    val lat: Double?,
    val lon: Double?,
    val name: String?,
    val state: String?
)