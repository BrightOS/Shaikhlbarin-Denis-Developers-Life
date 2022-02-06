package ru.brightos.shaikhlbarindenisdeveloperslife.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorInt

@ColorInt
fun getColorFromAttributes(context: Context?, res: Int): Int {
    val typedValue = TypedValue()
    val theme: Resources.Theme = context?.theme!!
    theme.resolveAttribute(res, typedValue, true)
    return typedValue.data
}