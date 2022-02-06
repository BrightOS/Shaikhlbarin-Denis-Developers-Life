package ru.brightos.shaikhlbarindenisdeveloperslife.data.repository

import ru.brightos.shaikhlbarindenisdeveloperslife.data.dao.PostDao
import ru.brightos.shaikhlbarindenisdeveloperslife.data.entity.PostEntity
import ru.brightos.shaikhlbarindenisdeveloperslife.models.PostModel
import java.util.*
import javax.inject.Inject

class LikedPostsRepositoryImpl @Inject constructor(private val dao: PostDao) :
    LikedPostsRepository {
    override fun getAllLikedPosts(): MutableList<PostModel> {
        val likedPostsList = arrayListOf<PostModel>()

        val postEntities = arrayListOf<PostEntity>()
        postEntities.addAll(dao.getAllLikedPosts())
        postEntities.sortBy { it.time }
        postEntities.reverse()
        postEntities.forEach {
            likedPostsList.add(
                PostModel(
                    it.id,
                    it.description,
                    it.votes,
                    it.author,
                    it.date,
                    it.gifURL,
                    it.gifSize,
                    it.previewURL,
                    it.type,
                    it.liked
                )
            )
        }

        return likedPostsList
    }

    override fun addLikedPost(item: PostModel) =
        item.let {
            dao.insertLikedPost(
                PostEntity(
                    it.id,
                    it.description,
                    it.votes,
                    it.author,
                    it.date,
                    it.gifURL,
                    it.gifSize,
                    it.previewURL,
                    it.type,
                    it.liked,
                    Calendar.getInstance().timeInMillis
                )
            )
        }

    override fun getLikedPostByID(id: Long): PostModel? =
        dao.getLikedPostByID(id).let {
            if (it.size > 0)
                it[0].let {
                    PostModel(
                        it.id,
                        it.description,
                        it.votes,
                        it.author,
                        it.date,
                        it.gifURL,
                        it.gifSize,
                        it.previewURL,
                        it.type,
                        it.liked
                    )
                }
            else
                null
        }

    override fun removePostFromLiked(id: Long) {
        dao.getLikedPostByID(id).let {
            if (it.size > 0)
                dao.deletePost(it[0])
        }
    }
}