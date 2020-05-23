package com.educatorapp.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.educatorapp.R
import com.educatorapp.data.preferences.PreferencesHelper
import com.educatorapp.databinding.FragmentProfileBinding
import com.educatorapp.ui.base.BaseFragment
import com.educatorapp.ui.login.LoginActivity
import com.educatorapp.utils.clients.GoogleSign
import com.educatorapp.utils.extensions.loadProfileUrl
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.shreyaspatil.MaterialDialog.MaterialDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment :
    BaseFragment<ProfileViewModel, FragmentProfileBinding>(R.layout.fragment_profile) {

    override val mViewModel: ProfileViewModel by viewModels()
    private val preferencesHelper: PreferencesHelper by inject() // Property Injection

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*mViewBinding.apply {
            lifecycleOwner = lifecycleOwner
            viewModel = mViewModel
        }*/

        /** Set options menu */
        setHasOptionsMenu(true)

        // Notes: never read preferences data from Main thread otherwise your UI hang and you can see "the application may be doing too much work on its main thread" message in your logcat
        lifecycleScope.launch(Dispatchers.IO) {
            val mobileUser = async {
                preferencesHelper.mobileUser
            }
            val data = mobileUser.await()
            withContext(Dispatchers.Main) {
                mViewBinding.userProfile.loadProfileUrl(data?.photoUrl.toString())
                mViewBinding.txtName.text = data?.displayName.toString()
                mViewBinding.txtEmail.text =
                    if (data?.email.toString().isNullOrBlank()) "N/A" else data?.email.toString()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate menu resource file.
        inflater.inflate(R.menu.logout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                confirmDialog()
            }
            R.id.menu_contact_us -> {
                findNavController().navigate(R.id.action_contact_us_screen)
            }
        }
        return true
    }

    fun confirmDialog() {
        MaterialDialog.Builder(requireActivity())
            .setTitle("Logout?")
            .setMessage("Are you sure want to logout?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                dialogInterface.dismiss()
                /** reset system and go back to Login Activity **/
                preferencesHelper.reset()

                val mGoogleSignInClient =
                    GoogleSignIn.getClient(requireActivity(), GoogleSign.googleSignInOptions)
                mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
                    requireActivity().startActivity(
                        LoginActivity.getIntent()
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }

            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .build()
            .show()
    }
}