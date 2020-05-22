package com.educatorapp.model

import android.os.Parcelable
import com.educatorapp.utils.TimeHelper
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Subject(
    var Id: String = "",
    var title: String = "",
    var description: String = "",
    var iconUrl: String = "",
    var educatorCount: Int = 0,
    var videosCount: Int = 0,
    var createdAt: String? = TimeHelper.getCurrentTimeString()
) : Parcelable