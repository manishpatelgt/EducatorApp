package com.educatorapp.application

import android.app.Application
import android.content.Context
import androidx.multidex.BuildConfig
import com.educatorapp.di.appModule
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        initKoin()

        /**
         * STETHO
         * */
        Stetho.initializeWithDefaults(this)

        /**
         * TIMBER
         * */

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(appModule)
        }
    }

    companion object {
        lateinit var appContext: Context
    }
}