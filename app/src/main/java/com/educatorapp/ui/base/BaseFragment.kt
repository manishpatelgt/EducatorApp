package com.educatorapp.ui.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.educatorapp.R
import com.educatorapp.fragments.NoDataFoundFragment
import com.educatorapp.fragments.dialogs.ProgressDialogFragment
import com.educatorapp.utils.Utility
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import org.koin.android.viewmodel.compat.ScopeCompat.getViewModel

abstract class BaseFragment<VM : ViewModel, T : ViewDataBinding>(@LayoutRes val layout: Int) : Fragment() {

    protected abstract val mViewModel: VM
    protected lateinit var mViewBinding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /** Lock fragment in portrait screen orientation */
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mViewBinding = DataBindingUtil.inflate(inflater, layout, container, false)
        return mViewBinding.root
    }

    var progressDialog: ProgressDialogFragment? = null

    fun showFragment(message_1: String, message_2: String) {
        val noDataFoundFragment =
            NoDataFoundFragment.create(
                message_1,
                message_2
            )
        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container_view, noDataFoundFragment)
        ft.commitAllowingStateLoss()
    }

    fun showProgress(show: Boolean) {
        if (show) {
            if (progressDialog == null) {
                progressDialog = ProgressDialogFragment.create()
                progressDialog!!.show(
                    childFragmentManager,
                    DIALOG_PROGRESS
                )
            }
        } else {
            val progressDialogFragment =
                childFragmentManager.findFragmentByTag(DIALOG_PROGRESS) as ProgressDialogFragment?
            if (progressDialogFragment != null && progressDialogFragment.isVisible) {
                progressDialog = null
                progressDialogFragment.dismissAllowingStateLoss()
            }
        }
    }

    fun rationaleCallback(req: QuickPermissionsRequest, permission: String) {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.permission_title2))
            .setMessage(Utility.getRationaleMessage(permission))
            .setCancelable(true)
            .setPositiveButton(android.R.string.yes) { dialog, _ -> req.proceed() }
            .create()
            .show()
    }

    fun permissionsPermanentlyDenied(req: QuickPermissionsRequest) {
        val alertBuilder = AlertDialog.Builder(requireActivity())
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle(getString(R.string.permission_title2))
        alertBuilder.setMessage(getString(R.string.permission_message2))
        alertBuilder.setPositiveButton(getString(R.string.go_to_settings)) { dialog, which ->
            req.openAppSettings()
        }
        val alert = alertBuilder.create()
        alert.show()
    }

    companion object {
        val DIALOG_PROGRESS = "DialogProgress"
    }
}