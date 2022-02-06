package ru.brightos.shaikhlbarindenisdeveloperslife.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.brightos.shaikhlbarindenisdeveloperslife.data.dao.PostDao
import ru.brightos.shaikhlbarindenisdeveloperslife.data.entity.PostEntity

@Database(entities = arrayOf(PostEntity::class), version = 1, exportSchema = false)
abstract class LikedPostsDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}