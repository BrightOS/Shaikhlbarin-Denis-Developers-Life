package ru.brightos.shaikhlbarindenisdeveloperslife.util.api.random

import android.content.Context
import android.util.Log
import ru.brightos.shaikhlbarindenisdeveloperslife.util.api.Response
import ru.followy.util.followy_extensions.api.APIConfig
import ru.followy.util.followy_extensions.api.APIUtils

class RandomPost(private val context: Context) {
    fun get() =
        APIUtils.get(context, "random?json=true", APIUtils.JSON)
}