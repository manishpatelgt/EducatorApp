package com.educatorapp.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.educatorapp.R
import com.educatorapp.databinding.FragmentProfileBinding
import com.educatorapp.ui.base.BaseFragment
import com.educatorapp.ui.login.LoginActivity
import com.educatorapp.utils.clients.GoogleSign
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.shreyaspatil.MaterialDialog.MaterialDialog
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment :
    BaseFragment<ProfileViewModel, FragmentProfileBinding>(R.layout.fragment_profile) {

    override val mViewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.apply {
            lifecycleOwner = lifecycleOwner
            viewModel = mViewModel
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
                mViewModel.logout()

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