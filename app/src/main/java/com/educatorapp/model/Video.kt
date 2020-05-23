package com.educatorapp.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.educatorapp.utils.TimeHelper
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video (
    var Id: String = "",
    var title: String = "",
    var description: String = "",
    var iconUrl: String = "",
    var videoUrl: String = "",
    var totalComment: Int = 0,
    var totalLikes: Int = 0,
    var subjectId: String = "",
    var educatorId: String = "",
    var rating : Float = 0.0F,
    var createdAt: String = TimeHelper.getCurrentTimeString()
): Parcelable