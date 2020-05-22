package com.educatorapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.educatorapp.data.preferences.PreferencesHelper
import com.educatorapp.model.MobileUser
import com.educatorapp.utils.network.isNetworkAvailable

/**
 * Created by Manish Patel on 4/20/2020.
 */
class MainViewModel : ViewModel() {

    /** Check network and cache conditions */
    val network = (isNetworkAvailable())

    /** Show a toast message */
    val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String>
        get() = _showToast

    /**
     * Show message in toast
     * */
    fun setToastMessage(message: String) {
        _showToast.value = message
    }
}