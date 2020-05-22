package com.educatorapp.ui.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.educatorapp.data.database.VideoDao
import kotlinx.coroutines.Dispatchers

class FavoriteViewModel constructor(private val videoDao: VideoDao) : ViewModel() {

    val favoriteVideoList = liveData(Dispatchers.IO) {
        val posts = videoDao.getAllVideo()
        emitSource(posts)
    }

}