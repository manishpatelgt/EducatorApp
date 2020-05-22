package com.educatorapp.utils.clients

import com.educatorapp.R
import com.educatorapp.application.App.Companion.appContext
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object GoogleSign {

    var googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(appContext.getString(R.string.default_web_client_id))
        .requestEmail()
        .requestId()
        .requestProfile()
        .build()

}