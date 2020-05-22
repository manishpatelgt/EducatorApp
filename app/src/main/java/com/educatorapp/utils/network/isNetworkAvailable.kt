package com.educatorapp.utils.network

import com.educatorapp.application.App.Companion.appContext

fun isNetworkAvailable(): Boolean = NetworkHelper.connectedToWiFiOrMobile(appContext)