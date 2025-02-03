package com.humanforce.humanforceandroidengineeringchallenge.data.locations

import android.annotation.SuppressLint
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.humanforce.humanforceandroidengineeringchallenge.common.threading.AppCoroutineDispatchers
import com.humanforce.humanforceandroidengineeringchallenge.data.db.SavedLocationDatabase
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.model.CurrentLocationData
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.model.SavedLocationData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface LocationsRepository {

    suspend fun saveLocation(savedLocationData: SavedLocationData)

    suspend fun deleteLocation(latitude: Double, longitude: Double)

    fun getAllLocations(): Flow<List<SavedLocationData>>

    suspend fun isLocationSaved(latitude: Double, longitude: Double): Boolean

    suspend fun getCurrentLocation(): CurrentLocationData?
}

@Singleton
class LocationsRepositoryImpl @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val database: SavedLocationDatabase,
    private val locationManager: LocationManager
) : LocationsRepository {

    override suspend fun saveLocation(
        savedLocationData: SavedLocationData
    ) {
        withContext(appCoroutineDispatchers.io) {
            database.savedLocationQueries.insertLocation(
                name = savedLocationData.name,
                state = savedLocationData.state,
                country = savedLocationData.country,
                latitude = savedLocationData.latitude,
                longitude = savedLocationData.longitude
            )
        }
    }

    override suspend fun deleteLocation(latitude: Double, longitude: Double) {
        withContext(appCoroutineDispatchers.io) {
            database.savedLocationQueries.deleteLocation(latitude, longitude)
        }
    }

    override fun getAllLocations(): Flow<List<SavedLocationData>> {
        return database.savedLocationQueries.getAllLocations()
            .asFlow()
            .mapToList(appCoroutineDispatchers.io)
            .map { savedLocations ->
                savedLocations.map { savedLocation ->
                    SavedLocationData(
                        name = savedLocation.name,
                        state = savedLocation.state,
                        country = savedLocation.country,
                        latitude = savedLocation.latitude,
                        longitude = savedLocation.longitude
                    )
                }
            }
    }

    override suspend fun isLocationSaved(latitude: Double, longitude: Double): Boolean {
        return withContext(appCoroutineDispatchers.io) {
            database.savedLocationQueries.isLocationSaved(
                latitude = latitude,
                longitude = longitude
            )
                .executeAsOneOrNull() ?: false
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): CurrentLocationData? {
        val location = locationManager.getCurrentLocation()
        // Get location name via Geocoder, so that name can be displayed in Home screen title
        return location?.let {
            locationManager.getLocationName(it.latitude, it.longitude)
        }?.run {
            CurrentLocationData(
                name = this,
                latitude = location.latitude,
                longitude = location.longitude
            )
        }
    }
}