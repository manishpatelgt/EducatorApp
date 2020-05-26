package com.educatorapp.ui.videoplayer

import androidx.lifecycle.*
import com.educatorapp.data.database.VideoDao
import com.educatorapp.data.preferences.PreferencesHelper
import com.educatorapp.model.MobileUser
import com.educatorapp.model.Video
import com.educatorapp.model.VideoComment
import com.educatorapp.model.VideoEntity
import com.educatorapp.utils.enums.State
import com.educatorapp.utils.extensions.asDatabaseModel
import com.educatorapp.utils.extensions.asDatabaseModel2
import com.educatorapp.utils.network.isNetworkAvailable
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VideoPlayViewModel constructor(
    val preferencesHelper: PreferencesHelper,
    private val videoDao: VideoDao
) : ViewModel() {

    private var mDatabase: DatabaseReference = Firebase.database.reference
    lateinit var mobileUser: MobileUser

    private val _id = MutableLiveData<String>()
    val id: LiveData<String> = _id

    var _videoComments: MutableLiveData<List<VideoComment>> = MutableLiveData()
    val videoComments: LiveData<List<VideoComment>>
        get() = _videoComments

    var _isSubmitted = MutableLiveData<Boolean>()
    val isSubmitted: LiveData<Boolean>
        get() = _isSubmitted

    private val _status = MutableLiveData<State>()
    val status: LiveData<State>
        get() = _status

    init {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesHelper.mobileUser?.let {
                mobileUser = it
            }
        }
    }

    /** Check video exist with like or for video exist with like */
    val selectedVideo: LiveData<VideoEntity> = id.switchMap { id ->
        liveData {
            emitSource(videoDao.getVideo(id))
        }
    }

    fun setVideoId(id: String) {
        _id.value = id
    }

    fun setVideoLike(video: Video, isLike: Boolean) {
        viewModelScope.launch {
            if (selectedVideo.value == null) {
                /** make new entry **/
                val videoEntity = video.asDatabaseModel(isLike)
                videoDao.insert(videoEntity)
            } else {
                /** update entry**/
                selectedVideo.value?.let {
                    it.isLike = isLike
                    videoDao.update(it)
                }
            }
        }
    }

    fun setVideoFavorite(video: Video, isFavorite: Boolean) {
        viewModelScope.launch {
            if (selectedVideo.value == null) {
                /** make new entry **/
                val videoEntity = video.asDatabaseModel2(isFavorite)
                videoDao.insert(videoEntity)
            } else {
                /** update entry**/
                selectedVideo.value?.let {
                    it.isFavorite = isFavorite
                    videoDao.update(it)
                }
            }
        }
    }

    fun getVideoComments(key: String) {
        _status.value = State.LOADING

        if (!isNetworkAvailable()) {
            _status.value = State.NOINTERNET
            return
        }

        val query: Query = mDatabase
            .child("Videos")
            .child(key)
            .child("Comments")
            .orderByChild("createdAt")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _status.value = State.DONE
                if (dataSnapshot.exists()) {
                    val mVideoComments: MutableList<VideoComment> = mutableListOf()
                    for (snapshot in dataSnapshot.children) {
                        val videoComment: VideoComment? =
                            snapshot.getValue(VideoComment::class.java)
                        videoComment?.let {
                            mVideoComments.add(it)
                        }
                    }
                    _videoComments.value = mVideoComments
                    if (mVideoComments.isEmpty()) {
                        _status.value = State.NODATA
                    }
                } else {
                    _status.value = State.NODATA
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _status.value = State.ERROR
            }
        })
    }

    fun submitComment(
        comment: String,
        key: String,
        videoId: String,
        subjectId: String,
        educatorId: String
    ) {
        val videoComment = VideoComment(
            videoId = videoId,
            subjectId = subjectId,
            educatorId = educatorId,
            userId = mobileUser.userId,
            email = mobileUser.email,
            displayName = mobileUser.displayName,
            photoUrl = mobileUser.photoUrl,
            comment = comment
        )

        mDatabase.child("Videos").child(key).child("Comments").child(videoComment.Id)
            .setValue(videoComment).addOnCompleteListener {
            _isSubmitted.value = true
        }
    }

    fun deleteComment(videoComment: VideoComment, key: String) {
        mDatabase.child("Videos").child(key).child("Comments").child(videoComment.Id).removeValue()
            .addOnCompleteListener {
                _isSubmitted.value = true
            }
    }

    fun editComment(videoComment: VideoComment, key: String) {
        mDatabase.child("Videos").child(key).child("Comments").child(videoComment.Id).setValue(videoComment)
            .addOnCompleteListener {
                _isSubmitted.value = true
            }
    }

    fun resetSubmit() {
        _isSubmitted.value = false
    }
}