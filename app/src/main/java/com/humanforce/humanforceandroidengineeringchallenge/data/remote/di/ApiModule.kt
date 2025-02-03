package com.humanforce.humanforceandroidengineeringchallenge.data.remote.di

import com.humanforce.humanforceandroidengineeringchallenge.data.remote.OpenWeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val OPENWEATHER_URL = "https://api.openweathermap.org/"

    private val jsonConfig = Json { ignoreUnknownKeys = true }

    private val kotlinxSerializationFactory =
        jsonConfig.asConverterFactory("application/json".toMediaType())

    @Provides
    @Singleton
    fun provideOpenWeatherApi(
        okHttpClient: OkHttpClient
    ): OpenWeatherApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(OPENWEATHER_URL)
            .addConverterFactory(kotlinxSerializationFactory)
            .build()
            .create(OpenWeatherApi::class.java)
    }

}