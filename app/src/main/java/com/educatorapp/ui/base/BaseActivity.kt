package com.educatorapp.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.educatorapp.R
import com.educatorapp.fragments.NoDataFoundFragment
import com.educatorapp.fragments.dialogs.ProgressDialogFragment
import es.dmoral.toasty.Toasty

abstract class BaseActivity<VM : ViewModel, VB : ViewBinding> : AppCompatActivity() {

    protected abstract val mViewModel: VM
    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = getViewBinding()
    }

    abstract fun getViewBinding(): VB

    var progressDialog: ProgressDialogFragment? = null
    fun showProgress(show: Boolean) {
        if (show) {
            if (progressDialog == null) {
                progressDialog = ProgressDialogFragment.create()
                progressDialog!!.show(
                    supportFragmentManager,
                    DIALOG_PROGRESS
                )
            }
        } else {
            val progressDialogFragment =
                supportFragmentManager.findFragmentByTag(DIALOG_PROGRESS) as ProgressDialogFragment?
            if (progressDialogFragment != null && progressDialogFragment.isVisible) {
                progressDialog = null
                progressDialogFragment.dismissAllowingStateLoss()
            }
        }
    }

    fun showFragment(message_1: String, message_2: String) {
        val noDataFoundFragment =
            NoDataFoundFragment.create(
                message_1,
                message_2
            )
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container_view, noDataFoundFragment)
        ft.commitAllowingStateLoss()
    }

    /** Show toast message */
    fun showToastMessage(toastMessage: String) {
        Toasty.info(this, toastMessage, Toast.LENGTH_LONG, false).show()
    }

    companion object {
        val DIALOG_PROGRESS = "DialogProgress"
    }
}