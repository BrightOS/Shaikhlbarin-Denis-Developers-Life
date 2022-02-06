package ru.brightos.shaikhlbarindenisdeveloperslife.util.api.top

import android.content.Context
import ru.followy.util.followy_extensions.api.APIUtils

class TopPosts(
    private val context: Context,
    private val page: Int,
    private val offset: Int
) {
    fun get() =
        APIUtils.get(context, "top/$page?json=true&pageSize=$offset", APIUtils.JSON)
}