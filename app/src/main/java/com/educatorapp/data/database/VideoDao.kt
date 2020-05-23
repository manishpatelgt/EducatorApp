package com.educatorapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.educatorapp.model.VideoEntity

@Dao
interface VideoDao {

    @Query("SELECT * from tbl_Videos WHERE isFavorite == 1")
    fun getAllVideo(): LiveData<List<VideoEntity>>

    @Insert
    suspend fun insert(video: VideoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<VideoEntity>)

    @Update
    suspend fun update(video: VideoEntity)

    @Delete
    suspend fun delete(video: VideoEntity)

    @Query("SELECT * FROM tbl_Videos WHERE Id=:Id")
    fun getVideo(Id: String): LiveData<VideoEntity>

    @Query("DELETE FROM tbl_Videos")
    suspend fun deleteAll()

}