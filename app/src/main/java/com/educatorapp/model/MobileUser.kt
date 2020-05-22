package com.educatorapp.model

import com.beust.klaxon.Json

data class MobileUser(

    @Json("userId")
    var userId: String? = "",

    @Json("email")
    var email: String? = "",

    @Json("createdAt")
    var createdAt: String = "",

    @Json("photoUrl")
    var photoUrl: String? = "",

    @Json("givenName")
    var givenName: String? = "",

    @Json("displayName")
    var displayName: String? = ""

)