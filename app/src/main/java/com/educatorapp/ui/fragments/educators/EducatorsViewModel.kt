package com.educatorapp.ui.fragments.educators

import android.util.Log
import androidx.lifecycle.*
import com.educatorapp.model.Educator
import com.educatorapp.model.Subject
import com.educatorapp.model.VideoEntity
import com.educatorapp.utils.enums.State
import com.educatorapp.utils.network.isNetworkAvailable
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EducatorsViewModel() : ViewModel() {

    val TAG = EducatorsViewModel::class.simpleName
    private var mDatabase: DatabaseReference = Firebase.database.reference

    private val _status = MutableLiveData<State>()
    val status: LiveData<State>
        get() = _status

    val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String>
        get() = _showToast

    var _educators: MutableLiveData<List<Educator>> = MutableLiveData()
    val educators: LiveData<List<Educator>>
        get() = _educators

    fun getEducators(subjectId: String?) {
        _status.value = State.LOADING

        if (!isNetworkAvailable()) {
            _status.value = State.NOINTERNET
            return
        }

        val query: Query = mDatabase
            .child("Educators")
            .orderByChild("subjectId")
            .equalTo(subjectId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _status.value = State.DONE
                if (dataSnapshot.exists()) {
                    val mEducators: MutableList<Educator> = mutableListOf()
                    for (snapshot in dataSnapshot.children) {
                        val educator: Educator? = snapshot.getValue(Educator::class.java)
                        educator?.let { mEducators.add(it) }
                    }
                    _educators.value = mEducators
                } else {
                    _status.value = State.NODATA
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _status.value = State.ERROR
            }
        })
    }

}