package com.educatorapp.ui.fragments.educatorvideo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.educatorapp.model.Video
import com.educatorapp.utils.enums.State
import com.educatorapp.utils.network.isNetworkAvailable
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EducatorVideoViewModel : ViewModel() {

    private var mDatabase: DatabaseReference = Firebase.database.reference

    private val _status = MutableLiveData<State>()
    val status: LiveData<State>
        get() = _status

    val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String>
        get() = _showToast

    var _videos: MutableLiveData<List<Video>> = MutableLiveData()
    val videos: LiveData<List<Video>>
        get() = _videos

    fun getVideos(educatorId: String?, subjectId: String?) {
        _status.value = State.LOADING

        if (!isNetworkAvailable()) {
            _status.value = State.NOINTERNET
            return
        }

        val query: Query = mDatabase
            .child("Videos")
            .orderByChild("educatorId")
            .equalTo(educatorId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _status.value = State.DONE

                if (dataSnapshot.exists()) {
                    val mVideos: MutableList<Video> = mutableListOf()
                    for (snapshot in dataSnapshot.children) {
                        val video: Video? = snapshot.getValue(Video::class.java)
                        video?.let {
                            /** filter only based on subject id **/
                            if (it.subjectId == subjectId) {
                                it.let { mVideos.add(it) }
                            }
                        }

                    }
                    _videos.value = mVideos
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