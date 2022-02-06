package ru.brightos.shaikhlbarindenisdeveloperslife.util.api.latest

import android.content.Context
import ru.followy.util.followy_extensions.api.APIUtils

class LatestPosts(
    private val context: Context,
    private val page: Int,
    private val offset: Int
) {
    fun get() =
        APIUtils.get(context, "latest/$page?json=true&pageSize=$offset", APIUtils.JSON)
}
