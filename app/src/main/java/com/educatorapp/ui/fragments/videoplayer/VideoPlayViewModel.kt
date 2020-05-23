package com.educatorapp.ui.fragments.videoplayer

import androidx.lifecycle.*
import com.educatorapp.data.database.VideoDao
import com.educatorapp.model.Video
import com.educatorapp.model.VideoEntity
import com.educatorapp.utils.extensions.asDatabaseModel
import com.educatorapp.utils.extensions.asDatabaseModel2
import kotlinx.coroutines.launch

class VideoPlayViewModel constructor(private val videoDao: VideoDao) : ViewModel() {

    private val _id = MutableLiveData<String>()
    val id: LiveData<String> = _id

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
}