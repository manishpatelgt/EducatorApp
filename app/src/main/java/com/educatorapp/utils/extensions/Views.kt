package com.educatorapp.utils.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.educatorapp.R

fun Context.inflate(res: Int, parent: ViewGroup? = null): View {
    return LayoutInflater.from(this).inflate(res, parent, false)
}

/**
 * Inflate the view from its layout and pass it back to a ViewHolder.
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

/**
 * Make a view visible by writing view.visible()
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Make a view gone.
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Make a view INVISIBLE by writing view.visible()
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadProfileUrl(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.profile_img)
        .into(this)
}