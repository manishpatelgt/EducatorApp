package com.educatorapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.educatorapp.utils.constants.Constants
import com.educatorapp.utils.network.isNetworkAvailable
import timber.log.Timber

/**
 * Created by Manish Patel on 4/20/2020.
 */
class MainViewModel(state: SavedStateHandle) : ViewModel() {

    val TAG = MainViewModel::class.simpleName
    private val savedStateHandle = state

    /** Show a toast message */
    val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String>
        get() = _showToast

    /** Fragment state  */
    private val _fragmentState = MutableLiveData<String>()
    val fragmentState: LiveData<String> = _fragmentState

    private val _fragmentStateHolder = MutableLiveData<String>()
    private val fragmentStateHolder: LiveData<String> = _fragmentStateHolder

    init {
        val savedState = getSavedStateHandle()
        Log.e(TAG, "Saved State : $savedState")
        setFragmentState(savedState)
    }

    /** Set saved state handle */
    private fun setSavedStateHandle() {
        // Sets a new value for the object associated to the key.
        fragmentStateHolder.value?.let { state ->
            Log.e(TAG, "Save State : $state")
            savedStateHandle.set(Constants.FRAGMENT_STATE, state)
        }
    }

    /** Get saved state handle  */
    private fun getSavedStateHandle(): String {
        // Gets the current value of the screen state from the saved state handle
        return savedStateHandle.get(Constants.FRAGMENT_STATE) ?: Constants.FRAGMENT_EMPTY
    }

    /** Set saved state handle for fragment state */
    private fun setFragmentState(state: String) {
        // Sets a new value for the object associated to the key.
        _fragmentState.value = state
    }

    /** Set saved state handle for fragment state holder */
    fun setFragmentStateHolder(stateHolder: String) {
        // Sets a new value for the object associated to the key.
        _fragmentStateHolder.value = stateHolder
    }

    override fun onCleared() {
        Log.e(TAG, "onCleared")
        setSavedStateHandle()
        super.onCleared()
    }

    /**
     * Show message in toast
     * */
    fun setToastMessage(message: String) {
        _showToast.value = message
    }
}