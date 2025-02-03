package com.humanforce.humanforceandroidengineeringchallenge.data.weather.di

import com.humanforce.humanforceandroidengineeringchallenge.data.weather.WeatherRepository
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherModule {

    @Binds
    abstract fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

}