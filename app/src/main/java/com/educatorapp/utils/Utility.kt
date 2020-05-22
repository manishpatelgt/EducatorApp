package com.educatorapp.utils

import android.Manifest

object Utility {

    fun getRationaleMessage(permission: String): String {
        return when (permission) {
            Manifest.permission.CALL_PHONE -> "This app needs permission to call support team automatically"
            else -> return "Rationale Message NOT Found!"
        }
    }

}