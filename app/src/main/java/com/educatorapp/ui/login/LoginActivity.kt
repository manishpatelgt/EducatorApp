package com.educatorapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.educatorapp.R
import com.educatorapp.application.App.Companion.appContext
import com.educatorapp.data.preferences.PreferencesHelper
import com.educatorapp.databinding.ActivityLoginBinding
import com.educatorapp.model.MobileUser
import com.educatorapp.ui.base.BaseActivity
import com.educatorapp.ui.main.MainActivity
import com.educatorapp.utils.TimeHelper
import com.educatorapp.utils.clients.GoogleSign
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import es.dmoral.toasty.Toasty
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class LoginActivity : BaseActivity<LoginModel, ActivityLoginBinding>() {

    private val preferencesHelper: PreferencesHelper by inject() // Property Injection

    override fun getViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override val mViewModel: LoginModel by viewModels()

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var mDatabase: DatabaseReference
    private var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        setupUI()
    }

    private fun setupUI() {

        mDatabase = Firebase.database.reference
        mViewBinding.signInButton.setSize(SignInButton.SIZE_WIDE)
        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSign.googleSignInOptions)
        mViewBinding.signInButton.setOnClickListener {
            if (!mViewModel.network) {
                mViewModel.setToastMessage(getString(R.string.no_internet_connection))
            }
            signIn()
        }

        /** Set observers*/
        setObservers()
    }


    private fun setObservers() {
        /** Set observer for a toast message */
        mViewModel.showToast.observe(this, Observer {
            showToastMessage(it)
        })
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            // Google Sign In was successful
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            account = completedTask.getResult(ApiException::class.java)
            checkUser()
        } catch (e: ApiException) {
            Timber.e("signInResult:failed code=" + e.statusCode)
            showToastMessage(
                "Google SignIn failed with due to " + CommonStatusCodes.getStatusCodeString(
                    e.statusCode
                )
            )
        }
    }

    private fun checkUser() {
        account?.let {

            preferencesHelper.isLogin = true

            //Check user already exist or not
            val query: Query = mDatabase
                .child("Users")
                .orderByChild("email")
                .equalTo(it.email)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.childrenCount > 0) {
                        //User already found
                        if (BuildConfig.DEBUG) {
                            Timber.e("User already found in firebase")
                        }
                        for (data in dataSnapshot.children) {
                            val mobileUser = data.getValue(MobileUser::class.java)
                            //Update details
                            updateUser(mobileUser)
                        }
                    } else {
                        //User not found in firebase, create new one
                        createNewUser()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    if (BuildConfig.DEBUG) {
                        Timber.e("Something went wrong! ${databaseError.message}")
                    }
                }
            })
        }
    }

    private fun createNewUser() {
        account?.let {
            val userId: String = UUID.randomUUID().toString()
            val mobileUser = MobileUser(
                it.id,
                it.email,
                TimeHelper.getCurrentTimeString(),
                it.photoUrl?.toString(),
                it.givenName,
                it.displayName
            )

            //Create User
            mDatabase.child("Users").child(userId).setValue(mobileUser)

            //Save data in Preferences
            preferencesHelper.mobileUser = mobileUser
            updateUI(it)
        }
    }

    private fun updateUser(mobileUser: MobileUser?) {
        //Save data in Preferences
        mobileUser?.userId = account!!.id
        preferencesHelper.mobileUser = mobileUser
        updateUI(account)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        account?.let {
            /** move to MainActivity **/
            startActivity(MainActivity.getIntent())
            this@LoginActivity.finish()
        }
    }

    /** Show toast message */
    fun showToastMessage(toastMessage: String) {
        Toasty.info(this, toastMessage, Toast.LENGTH_LONG, false).show()
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent,
            RC_SIGN_IN
        )
    }

    companion object {
        fun getIntent() = Intent(appContext, LoginActivity::class.java)
        private const val RC_SIGN_IN = 9001
    }
}