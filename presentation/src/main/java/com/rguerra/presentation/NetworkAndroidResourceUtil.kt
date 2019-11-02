package com.rguerra.presentation

import android.net.ConnectivityManager

class NetworkAndroidResourceUtil(private val connectivityManager: ConnectivityManager) {

    fun isNetworkConnected(): Boolean {
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }
}