package com.educatorapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.educatorapp.utils.network.isNetworkAvailable

class LoginModel : ViewModel() {

    /** Show a toast message */

    val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String>
        get() = _showToast

    /** Check network and cache conditions */
    val network = (isNetworkAvailable())

    /**
     * Show message in toast
     * */
    fun setToastMessage(message: String) {
        _showToast.value = message
    }
}