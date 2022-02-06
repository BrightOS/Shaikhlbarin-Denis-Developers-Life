package ru.brightos.shaikhlbarindenisdeveloperslife.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_posts")
data class PostEntity(

    @PrimaryKey(autoGenerate = false)
    var id: Long,
    var description: String,
    val votes: Long,
    var author: String,
    var date: String,
    var gifURL: String,
    var gifSize: Long,
    var previewURL: String,
    var type: String,
    var liked: Boolean,
    var time: Long

)