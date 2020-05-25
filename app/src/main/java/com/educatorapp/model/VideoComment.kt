package com.educatorapp.model

import com.educatorapp.utils.TimeHelper
import java.util.*

data class VideoComment(
    var Id: String = UUID.randomUUID().toString(),
    var videoId: String = "",
    var subjectId: String = "",
    var educatorId: String = "",
    var userId: String? = "",
    var email: String? = "",
    var displayName: String? = "",
    var photoUrl: String? = "",
    var comment: String="",
    var createdAt: String? = TimeHelper.getCurrentTimeString()
)