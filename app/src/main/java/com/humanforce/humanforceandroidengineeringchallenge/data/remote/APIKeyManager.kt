package com.humanforce.humanforceandroidengineeringchallenge.data.remote

object APIKeyManager {

    private var _apiKey: String = com.humanforce.humanforceandroidengineeringchallenge.BuildConfig.OPEN_WEATHER_MAP_API_KEY

    val apiKey: String
        get() = _apiKey.takeIf { it.isNotEmpty() }
            ?: throw IllegalStateException("API key not found. Please ensure .env file is properly configured.")
}