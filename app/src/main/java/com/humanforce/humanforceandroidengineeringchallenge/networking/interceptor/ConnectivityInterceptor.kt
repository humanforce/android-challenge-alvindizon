package com.humanforce.humanforceandroidengineeringchallenge.networking.interceptor

import com.humanforce.humanforceandroidengineeringchallenge.networking.connectivity.ConnectivityMonitor
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

class NoInternetException : IOException("No internet connection")

@Singleton
class ConnectivityInterceptor
@Inject
constructor(private val connectivityMonitor: ConnectivityMonitor) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: IOException) {
            if (!connectivityMonitor.isConnected) {
                throw NoInternetException()
            } else {
                throw e
            }
        }
    }
}
