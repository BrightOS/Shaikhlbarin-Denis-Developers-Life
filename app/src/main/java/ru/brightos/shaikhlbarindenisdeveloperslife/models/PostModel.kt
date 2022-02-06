package ru.brightos.shaikhlbarindenisdeveloperslife.models

class PostModel(
    val id: Long,
    val description: String,
    val votes: Long,
    val author: String,
    val date: String,
    val gifURL: String,
    val gifSize: Long,
    val previewURL: String,
    val type: String,
    val liked: Boolean
)