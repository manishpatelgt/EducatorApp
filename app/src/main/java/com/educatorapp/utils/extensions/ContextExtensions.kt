package com.educatorapp.utils.extensions

import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.telephony.TelephonyManager
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.educatorapp.R

fun Context.makePhoneCall(number: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun Context.recyclerDivider() = DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
    drawable(R.drawable.list_divider_2)?.let { setDrawable(it) }
}