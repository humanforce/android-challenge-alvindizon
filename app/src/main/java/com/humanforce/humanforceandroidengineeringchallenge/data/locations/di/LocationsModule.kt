package com.humanforce.humanforceandroidengineeringchallenge.data.locations.di

import com.humanforce.humanforceandroidengineeringchallenge.data.locations.LocationsRepository
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.LocationsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationsModule {

    @Binds
    abstract fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository
}