package com.educatorapp.ui.fragments.contactus

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.educatorapp.R
import com.educatorapp.databinding.FragmentContactBinding
import com.educatorapp.ui.base.BaseFragment
import com.educatorapp.ui.main.MainViewModel
import com.educatorapp.utils.Utility
import com.educatorapp.utils.extensions.isAtLeastAndroid6
import com.educatorapp.utils.extensions.makePhoneCall
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by Manish Patel on 4/24/2020.
 */
class ContactFragment :
    BaseFragment<ContactViewModel, FragmentContactBinding>(R.layout.fragment_contact),
    View.OnClickListener {
    private val sharedViewModel: MainViewModel by sharedViewModel()
    override val mViewModel: ContactViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** button click listeners **/
        mViewBinding.iconEmailSmall.setOnClickListener(this)
        mViewBinding.iconPhoneSmall.setOnClickListener(this)
        mViewBinding.submitBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.icon_email_small -> {
                requireActivity()?.let {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:")
                    intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.support_email))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                    if (intent.resolveActivity(it.packageManager) != null) {
                        it.startActivity(intent)
                    }
                }
            }
            R.id.icon_phone_small -> {
                /** check for call permission **/
                askForLocationPermissions()
            }
            R.id.submit_btn -> {
                /** check for validation **/
                val subject = mViewBinding.editTextSubject.text.toString()
                val message = mViewBinding.editTextMessage.text.toString()

                if (subject.isEmpty()) {
                    sharedViewModel.setToastMessage("Enter subject")
                    return
                }

                if (message.isEmpty()) {
                    sharedViewModel.setToastMessage("Enter message")
                    return
                }

                /** store value in firebase **/
                mViewModel.saveFeedback(subject, message)
                sharedViewModel.setToastMessage("Your feedback submitted successfully. Thank you!")
                resetUI()
            }
        }
    }

    fun resetUI() {
        mViewBinding.editTextSubject.text.clear()
        mViewBinding.editTextMessage.text.clear()
    }

    fun askForLocationPermissions() {
        if (isAtLeastAndroid6()) {
            val quickPermissionsOption = QuickPermissionsOptions(
                handleRationale = true,
                rationaleMessage = Utility.getRationaleMessage(Manifest.permission.CALL_PHONE),
                permanentlyDeniedMessage = getString(R.string.permission_message2),
                rationaleMethod = { req ->
                    rationaleCallback(
                        req,
                        Manifest.permission.CALL_PHONE
                    )
                },
                permanentDeniedMethod = { req -> permissionsPermanentlyDenied(req) }
            )

            runWithPermissions(
                Manifest.permission.CALL_PHONE,
                options = quickPermissionsOption
            ) {
                /** Process for call **/
                requireActivity()?.let {
                    it.makePhoneCall(it.getString(R.string.support_call))
                }
            }
        } else {
            /** Process for call **/
            requireActivity()?.let {
                it.makePhoneCall(it.getString(R.string.support_call))
            }
        }
    }
}