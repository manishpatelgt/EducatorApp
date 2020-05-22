package com.educatorapp.ui

import androidx.appcompat.app.AppCompatActivity
import com.educatorapp.application.App
import com.educatorapp.data.preferences.PreferencesHelper
import com.educatorapp.ui.login.LoginActivity
import com.educatorapp.ui.main.MainActivity
import org.koin.android.ext.android.inject

class StartUp : AppCompatActivity() {

    private val preferencesHelper: PreferencesHelper by inject() // Property Injection

    override fun onStart() {
        super.onStart()
        if (preferencesHelper.isLogin) {
            /** Move to MainActivity **/
            startActivity(MainActivity.getIntent())
        } else {
            /** Move to login Activity **/
            startActivity(LoginActivity.getIntent())
        }
        this@StartUp.finish()
    }
}