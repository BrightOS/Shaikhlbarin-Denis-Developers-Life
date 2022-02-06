package ru.brightos.shaikhlbarindenisdeveloperslife.data.repository

import ru.brightos.shaikhlbarindenisdeveloperslife.data.entity.PostEntity
import ru.brightos.shaikhlbarindenisdeveloperslife.models.PostModel

interface LikedPostsRepository {
    fun getAllLikedPosts() : List<PostModel>
    fun addLikedPost(item: PostModel)
    fun getLikedPostByID(id: Long) : PostModel?
    fun removePostFromLiked(id: Long)
}