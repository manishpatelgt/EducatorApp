package com.educatorapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_Videos")
data class VideoEntity(
    @PrimaryKey
    var Id: String = "",
    var title: String = "",
    var description: String = "",
    var iconUrl: String = "",
    var videoUrl: String = "",
    var totalComment: Int = 0,
    var totalLikes: Int = 0,
    var subjectId: String = "",
    var educatorId: String = "",
    var rating: Float = 0.0F,
    var isLike: Boolean = false,
    var key: String = "",
    var isFavorite: Boolean = false
)