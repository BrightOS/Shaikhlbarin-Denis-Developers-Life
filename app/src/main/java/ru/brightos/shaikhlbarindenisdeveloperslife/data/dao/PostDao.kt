package ru.brightos.shaikhlbarindenisdeveloperslife.data.dao

import androidx.room.*
import ru.brightos.shaikhlbarindenisdeveloperslife.data.entity.PostEntity

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLikedPost(item: PostEntity)

    @Query("SELECT * FROM liked_posts")
    fun getAllLikedPosts(): List<PostEntity>

    @Query("SELECT * FROM liked_posts WHERE id = :id")
    fun getLikedPostByID(id: Long): List<PostEntity>

    @Delete
    fun deletePost(item: PostEntity)
}