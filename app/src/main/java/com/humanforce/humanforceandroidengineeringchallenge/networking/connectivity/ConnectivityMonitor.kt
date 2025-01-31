package com.humanforce.humanforceandroidengineeringchallenge.networking.connectivity

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

internal data class NetworkState(val isConnected: Boolean, val isValidated: Boolean) {
    val isOnline
        get() = isConnected && isValidated
}

internal val Context.connectivityManager: ConnectivityManager
    get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

@Singleton
@SuppressLint("MissingPermission")
class ConnectivityMonitor @Inject constructor(@ApplicationContext context: Context) {

    private val _isConnected = AtomicBoolean(false)
    val isConnected
        get() = _isConnected.get()

    init {
        val connectivityManager = context.connectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val activeNetworkState = NetworkState(
            isConnected = when {
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> true
                else -> false
            },
            isValidated = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
        )
        val networkCallback =
            object : NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    if (activeNetworkState.isOnline) _isConnected.set(true)
                }

                override fun onLost(network: Network) {
                    if (!activeNetworkState.isOnline) _isConnected.set(false)
                }
            }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }
}



