package com.educatorapp.model

import android.os.Parcelable
import com.educatorapp.utils.TimeHelper
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Educator(
    var Id: String = "",
    var name: String = "",
    var information: String = "",
    var subjectId: String = "",
    var level: Int = 0,
    var videosCount: Int = 0,
    var rating: Float = 0.0F,
    var iconUrl: String = "",
    var createdAt: String? = TimeHelper.getCurrentTimeString()
): Parcelable