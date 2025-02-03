package com.humanforce.humanforceandroidengineeringchallenge.data.locations

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationTokenSource
import com.humanforce.humanforceandroidengineeringchallenge.common.threading.AppCoroutineDispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LocationManager {

    suspend fun getCurrentLocation(): Location?

    suspend fun getLocationName(latitude: Double, longitude: Double): String?

    suspend fun isLocationOn(locationEnableRequest: ActivityResultLauncher<IntentSenderRequest>?): Boolean


}

@Singleton
@SuppressLint("MissingPermission")
class LocationManagerImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val settingsClient: SettingsClient
) : LocationManager {

    private val locationRequest by lazy {
        LocationRequest.Builder(TimeUnit.SECONDS.toMillis(INTERVAL_SEC))
            .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(FASTEST_INTERVAL_SEC))
            .setMaxUpdateDelayMillis(TimeUnit.MINUTES.toMillis(MAX_WAIT_TIME_MIN))
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()
    }

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

    override suspend fun isLocationOn(locationEnableRequest: ActivityResultLauncher<IntentSenderRequest>?): Boolean =
        suspendCancellableCoroutine { cont ->
            val locationSettingsRequest =
                LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
            settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener { cont.resume(true) }
                .addOnCanceledListener { cont.resume(false) }
                .addOnFailureListener {
                    (it as? ResolvableApiException)?.let { resolvableE ->
                        showLocationServiceDialog(locationEnableRequest, resolvableE.resolution)
                    }
                    cont.resume(false)
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

    private fun showLocationServiceDialog(
        retryLocationEnableRequest: ActivityResultLauncher<IntentSenderRequest>?,
        resolution: PendingIntent
    ) {
        try {
            retryLocationEnableRequest?.launch(IntentSenderRequest.Builder(resolution).build())
        } catch (e: ActivityNotFoundException) {
            // ignore error
        }
    }

    companion object {
        private const val INTERVAL_SEC = 60L
        private const val FASTEST_INTERVAL_SEC = 30L
        private const val MAX_WAIT_TIME_MIN = 2L
    }
}