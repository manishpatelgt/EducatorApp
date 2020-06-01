package com.educatorapp.utils.network

import android.content.Context
import android.net.*
import android.os.Build
import com.educatorapp.utils.extensions.connectivityManager

object NetworkHelper {

    fun connectedToWiFiOrMobile(context: Context): Boolean {
        var isInternet = true

        val connectivityManager = context.connectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                isInternet = true
            }

            override fun onLost(network: Network?) {
                isInternet = false
            }
        }

        val activeNetwork: NetworkInfo? = connectivityManager?.activeNetworkInfo
        isInternet = activeNetwork?.isConnectedOrConnecting == true

        if (!isInternet) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager?.registerDefaultNetworkCallback(networkCallback)
            } else {
                val builder = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                connectivityManager?.registerNetworkCallback(builder.build(), networkCallback)
            }
        }

        return isInternet
    }
}