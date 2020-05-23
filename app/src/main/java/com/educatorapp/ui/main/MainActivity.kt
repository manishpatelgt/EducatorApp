package com.educatorapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.educatorapp.R
import com.educatorapp.application.App
import com.educatorapp.application.App.Companion.appContext
import com.educatorapp.databinding.ActivityMainBinding
import com.educatorapp.ui.base.BaseActivity
import com.educatorapp.utils.constants.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.dmoral.toasty.Toasty

/**
 * Created by Manish Patel on 5/21/2020.
 */
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override val mViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
        setupUI()
    }

    private fun setupUI() {

        /** Set action bar */
        setSupportActionBar(mViewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /** Set observers*/
        setObservers()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_subject,
                R.id.navigation_favorite,
                R.id.navigation_profile
            )
        )

        //NavigationUI.setupWithNavController(mViewBinding.toolbar, navController)
        navView.setupWithNavController(navController)
        mViewBinding.toolbar?.setupWithNavController(navController, appBarConfiguration)
        /*mViewBinding.navView.setOnNavigationItemReselectedListener {
            "Reselect blocked."
        }*/

        /*mViewBinding.toolbar.setNavigationOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.navigation_subject, R.id.navigation_favorite, R.id.navigation_profile -> {
                    Log.e("Main", "current destination id: ${navController.currentDestination?.id} ")
                    if (onBackPressedDispatcher.hasEnabledCallbacks())
                        onBackPressedDispatcher.onBackPressed()
                    else
                        navController.navigateUp()
                }
                else -> navController.navigateUp()
            }
        }*/

        /** Setup fragment state observer  */
        val fragmentStateObserver = Observer<String> { state ->
            when (state) {
                Constants.FRAGMENT_SUBJECTS -> navController.navigate(R.id.navigation_subject)
                Constants.FRAGMENT_FAVORITE -> navController.navigate(R.id.navigation_favorite)
                Constants.FRAGMENT_PROFILE -> navController.navigate(R.id.navigation_profile)
            }
        }
        mViewModel.fragmentState.observe(this, fragmentStateObserver)
    }

    private fun setObservers() {
        /** Set observer for a toast message */
        mViewModel.showToast.observe(this, Observer {
            showToastMessage(it)
        })
    }


    /** Show toast message */
    fun showToastMessage(toastMessage: String) {
        Toasty.info(this, toastMessage, Toast.LENGTH_LONG, false).show()
    }

    companion object {
        val TAG = MainActivity::class.simpleName
        fun getIntent() = Intent(appContext, MainActivity::class.java)
    }
}