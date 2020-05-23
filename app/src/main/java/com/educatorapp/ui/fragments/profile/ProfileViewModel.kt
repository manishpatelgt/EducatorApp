package com.educatorapp.ui.fragments.profile

import androidx.lifecycle.ViewModel
import com.educatorapp.data.preferences.PreferencesHelper
import com.educatorapp.model.MobileUser

class ProfileViewModel  : ViewModel() {

    /*lateinit var mobileUser: MobileUser

    init {
        preferencesHelper.mobileUser?.let {
            mobileUser = it
        }
    }

    fun setName(): String = mobileUser.displayName.toString()

    fun getProfileUrl(): String = mobileUser.photoUrl.toString()

    fun setEmail(): String =
        if (mobileUser.email.toString().isNullOrBlank()) "N/A" else mobileUser.email.toString()

    fun logout() {
        preferencesHelper.reset()
    }*/

}