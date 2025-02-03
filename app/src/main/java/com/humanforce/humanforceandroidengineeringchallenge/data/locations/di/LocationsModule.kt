package com.humanforce.humanforceandroidengineeringchallenge.data.locations.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.LocationManager
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.LocationManagerImpl
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.LocationsRepository
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.LocationsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationsModule {

    @Binds
    abstract fun bindLocationsRepository(impl: LocationsRepositoryImpl): LocationsRepository

    @Binds
    abstract fun bindLocationManager(impl: LocationManagerImpl): LocationManager

    companion object {
        @Provides
        fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(context)
        }

        @Provides
        fun provideGeocoder(@ApplicationContext context: Context): Geocoder {
            return Geocoder(context, Locale.getDefault())
        }
    }
}