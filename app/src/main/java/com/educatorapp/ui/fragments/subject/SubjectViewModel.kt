package com.educatorapp.ui.fragments.subject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.educatorapp.model.Subject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.educatorapp.utils.enums.State
import com.educatorapp.utils.network.isNetworkAvailable
import timber.log.Timber

class SubjectViewModel : ViewModel() {

    private var mDatabase: DatabaseReference = Firebase.database.reference

    private val _status = MutableLiveData<State>()
    val status: LiveData<State>
        get() = _status

    val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String>
        get() = _showToast

    var _subjects: MutableLiveData<List<Subject>> = MutableLiveData()
    val subjects: LiveData<List<Subject>>
        get() = _subjects

    init {
        getSubjects()
    }

    fun getSubjects() {

        _status.value = State.LOADING

        if (!isNetworkAvailable()) {
            _status.value = State.NOINTERNET
            return
        }

        val databaseReference = mDatabase.child("Subjects").orderByChild("title")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _status.value = State.DONE
                if (dataSnapshot.exists()) {
                    val mSubjects: MutableList<Subject> = mutableListOf()
                    for (snapshot in dataSnapshot.children) {
                        //val key = dataSnapshot.key
                        val subject: Subject? = snapshot.getValue(Subject::class.java)
                        subject?.let { mSubjects.add(it) }
                    }
                    _subjects.value = mSubjects
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _status.value = State.ERROR
            }
        })
    }

}