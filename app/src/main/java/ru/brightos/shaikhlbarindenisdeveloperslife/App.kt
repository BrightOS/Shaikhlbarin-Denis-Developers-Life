package ru.brightos.shaikhlbarindenisdeveloperslife

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.brightos.shaikhlbarindenisdeveloperslife.data.repository.PreferenceRepository

@HiltAndroidApp
class App : Application() {
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()

        preferenceRepository = PreferenceRepository(
            getSharedPreferences(DEFAULT_PREFERENCES, MODE_PRIVATE)
        )
    }

    companion object {
        const val DEFAULT_PREFERENCES = "default_devlife_preferences"
    }
}