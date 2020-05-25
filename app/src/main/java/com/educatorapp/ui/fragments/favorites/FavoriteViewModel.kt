package com.educatorapp.ui.fragments.favorites

import androidx.lifecycle.*
import com.educatorapp.data.database.VideoDao
import com.educatorapp.model.Video
import com.educatorapp.utils.extensions.asDatabaseModel2
import com.educatorapp.utils.extensions.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel constructor(private val videoDao: VideoDao) : ViewModel() {

    val favoriteVideoList = Transformations.map(videoDao.getAllVideo()) {
        it.asDomainModel()
    }

    fun removeVideoFavorite(video: Video) = viewModelScope.launch {
        /** update entry**/
        val videoEntity = video.asDatabaseModel2(false)
        videoDao.update(videoEntity)

    }

    /*val favoriteVideoList = liveData(Dispatchers.IO) {
        val posts = videoDao.getAllVideo()
        emitSource(posts)
    }*/

}