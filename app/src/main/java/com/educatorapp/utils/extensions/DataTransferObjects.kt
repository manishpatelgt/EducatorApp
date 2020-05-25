package com.educatorapp.utils.extensions

import com.educatorapp.model.Video
import com.educatorapp.model.VideoEntity

/**
 * Convert Database model to domain model objects and vice versa
 */

fun List<VideoEntity>.asDomainModel(): List<Video> {
    return map {
        Video(
            Id = it.Id,
            title = it.title,
            description = it.description,
            iconUrl = it.iconUrl,
            videoUrl = it.videoUrl,
            totalComment = it.totalComment,
            totalLikes = it.totalLikes,
            subjectId = it.subjectId,
            educatorId = it.educatorId,
            key = it.key,
            rating = it.rating
        )
    }
}

fun Video.asDatabaseModel(isLike: Boolean) =
    VideoEntity(
        Id = Id,
        title = title,
        description = description,
        iconUrl = iconUrl,
        videoUrl = videoUrl,
        totalComment = totalComment,
        totalLikes = totalLikes,
        subjectId = subjectId,
        educatorId = educatorId,
        rating = rating,
        key = key,
        isLike = isLike
    )


fun Video.asDatabaseModel2(isFavorite: Boolean) =
    VideoEntity(
        Id = Id,
        title = title,
        description = description,
        iconUrl = iconUrl,
        videoUrl = videoUrl,
        totalComment = totalComment,
        totalLikes = totalLikes,
        subjectId = subjectId,
        educatorId = educatorId,
        rating = rating,
        key = key,
        isFavorite = isFavorite
    )
