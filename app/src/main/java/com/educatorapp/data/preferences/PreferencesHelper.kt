package com.educatorapp.data.preferences

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.beust.klaxon.Klaxon
import com.educatorapp.model.MobileUser
import timber.log.Timber

class PreferencesHelper(val context: Context) {

    private val PREF_KEY_IS_LOGIN = "Is_Login_Verify"  // Is_Login
    private val PREF_KEY_MOBILE_USER = "MobileUser"

    var preferences: SharedPreferences =
        context.getSharedPreferences("AppPreferences", Activity.MODE_PRIVATE)

    private fun putString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    var isLogin: Boolean
        get() {
            return getBoolean(PREF_KEY_IS_LOGIN, false)
        }
        set(value) {
            putBoolean(PREF_KEY_IS_LOGIN, value)
        }


    private fun getString(key: String, defValue: String? = ""): String? {
        return if (preferences.getString(key, defValue).equals("null")) {
            defValue
        } else {
            preferences.getString(key, defValue)
        }
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putBoolean(key, value!!)
        editor.commit()
    }

    private fun getInt(key: String): Int {
        return preferences.getInt(key, 0)
    }

    private fun putInt(key: String, value: Int) {
        val editor = preferences.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    var mobileUser: MobileUser?
        get() = getObject(PREF_KEY_MOBILE_USER)
        set(mobileUser) = putString(PREF_KEY_MOBILE_USER, Klaxon().toJsonString(mobileUser))

    fun reset() {
        mobileUser = null
        isLogin = false
    }

    private inline fun <reified T> getObject(key: String): T? {
        return try {
            val json = getString(key)
            if (TextUtils.isEmpty(json) || json.equals("null")) null else Klaxon().parse<T>(json!!)
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
            null
        }
    }
}