package com.educatorapp.ui.fragments.contactus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educatorapp.data.preferences.PreferencesHelper
import com.educatorapp.model.Feedback
import com.educatorapp.model.MobileUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Manish Patel on 4/24/2020.
 */
class ContactViewModel constructor(val preferencesHelper: PreferencesHelper) : ViewModel() {

    lateinit var mobileUser: MobileUser

    init {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesHelper.mobileUser?.let {
                mobileUser = it
            }
        }
    }

    private var mDatabase: DatabaseReference = Firebase.database.reference

    fun saveFeedback(subject: String, message: String) {
        val Id = UUID.randomUUID().toString()
        val feedback = Feedback(Id, subject, message, mobileUser.userId, mobileUser.email)
        //Create Feedback
        mDatabase.child("Feedbacks").child(Id).setValue(feedback).addOnCompleteListener {}
    }

}