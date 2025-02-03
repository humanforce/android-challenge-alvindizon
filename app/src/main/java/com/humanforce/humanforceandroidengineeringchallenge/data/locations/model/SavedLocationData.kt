package com.humanforce.humanforceandroidengineeringchallenge.data.locations.model

import com.humanforce.humanforceandroidengineeringchallenge.ui.utils.toFlagEmoji

data class SavedLocationData(
    val name: String,
    val state: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
) {
    val formattedLocationName: String = if (state.isBlank()) {
        "$country ${country.toFlagEmoji()}"
    } else {
        "${state}, $country ${country.toFlagEmoji()}"
    }
}
