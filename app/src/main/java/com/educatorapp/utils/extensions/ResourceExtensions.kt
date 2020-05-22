package com.educatorapp.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun isAtLeastAndroid6() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
fun isAtLeastAndroid7() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
fun isAtLeastAndroid8() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
fun isAtLeastAndroid9() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P


/**
 * Retrieve a Color resource.
 *
 * @sample context.color(R.color.color2)
 */
fun Context.color(@ColorRes id: Int) = when {
    isAtLeastAndroid6() -> resources.getColor(id, null)
    else -> resources.getColor(id)
}

/**
 * Retrieve a Drawable based on drawable resource.
 */
fun Context.drawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

fun Context.bitmap(@DrawableRes resId: Int): Bitmap =
    BitmapFactory.decodeResource(this.resources, resId)

fun Context.getColorCompat(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

fun Context.getCustomColor(@ColorRes resId: Int) = when {
    isAtLeastAndroid6() -> ContextCompat.getColor(this, resId)
    else -> resources.getColor(resId)
}
