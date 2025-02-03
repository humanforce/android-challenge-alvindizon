package com.humanforce.humanforceandroidengineeringchallenge.data.locations

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.humanforce.humanforceandroidengineeringchallenge.common.threading.AppCoroutineDispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LocationManager {

    suspend fun getCurrentLocation(): Location?

    suspend fun getLocationName(latitude: Double, longitude: Double): String?

}

@Singleton
@SuppressLint("MissingPermission")
class LocationManagerImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val appCoroutineDispatchers: AppCoroutineDispatchers
) : LocationManager {

    override suspend fun getCurrentLocation(): Location? {
        return withContext(appCoroutineDispatchers.io) {
            suspendCancellableCoroutine { cont ->
                val cancellationTokenSource = CancellationTokenSource()
                // Try to get last known location, else, get fresh read via getCurrentLocation
                fusedLocationProviderClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            cont.resume(location)
                        } else {
                            fusedLocationProviderClient.getCurrentLocation(
                                Priority.PRIORITY_HIGH_ACCURACY,
                                cancellationTokenSource.token
                            ).addOnSuccessListener {
                                cont.resume(it)
                            }
                                .addOnCanceledListener { cont.resume(null) }
                                .addOnFailureListener { cont.resumeWithException(it) }
                        }
                    }.addOnFailureListener {
                        cont.resumeWithException(it)
                    }
                cont.invokeOnCancellation { cancellationTokenSource.cancel() }
            }
        }
    }

    override suspend fun getLocationName(latitude: Double, longitude: Double): String? {
        return withContext(appCoroutineDispatchers.io) {
            suspendCoroutine { cont ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        latitude,
                        longitude,
                        1,
                        object : Geocoder.GeocodeListener {
                            override fun onGeocode(addresses: MutableList<Address>) {
                                cont.resume(getLocationFromAddress(addresses))
                            }

                            override fun onError(errorMessage: String?) {
                                cont.resumeWithException(Exception(errorMessage))
                            }
                        })
                } else {
                    try {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                        cont.resume(getLocationFromAddress(addresses))
                    } catch (e: Exception) {
                        cont.resumeWithException(e)
                    }
                }
            }
        }
    }

    private fun getLocationFromAddress(addresses: List<Address>?): String? {
        var locationName: String? = null
        if (!addresses.isNullOrEmpty()) {
            val fetchedAddress = addresses[0]
            if (fetchedAddress.maxAddressLineIndex > -1) {
                locationName = fetchedAddress.locality ?: fetchedAddress.subLocality
                        ?: fetchedAddress.subAdminArea ?: fetchedAddress.adminArea
            }
        }
        return locationName
    }
}