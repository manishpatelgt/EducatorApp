package com.educatorapp.utils.clients

import com.educatorapp.model.Video
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import timber.log.Timber

object VideoClient {

    var mDatabase: DatabaseReference = Firebase.database.reference

    fun updateVideoLike(videoId: String, isLike: Boolean) {
        val query: Query = mDatabase
            .child("Videos")
            .orderByChild("id")
            .equalTo(videoId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (snapshot in dataSnapshot.children) {
                        val video: Video? = snapshot.getValue(Video::class.java)
                        video?.let {
                            var newLike = it.totalLikes
                            if (isLike) {
                                newLike += 1
                            } else {
                                newLike -= 1
                                if (newLike <= 0) {
                                    newLike = 0
                                }
                            }
                            /** update it **/
                            it.totalLikes = newLike
                            snapshot.ref.setValue(it)
                        }

                    }
                    Timber.d("Updated video like total count")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.e("Updating  video like error ${databaseError.message}")
            }
        })
    }
}