package com.educatorapp.model

import com.educatorapp.utils.TimeHelper

data class Feedback(
    var Id: String = "",
    var subject: String = "",
    var message: String = "",
    var userId: String? = "",
    var email: String? = "",
    var createdAt: String? = TimeHelper.getCurrentTimeString()
)