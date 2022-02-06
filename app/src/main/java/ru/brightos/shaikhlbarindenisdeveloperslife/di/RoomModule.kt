package ru.brightos.shaikhlbarindenisdeveloperslife.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.brightos.shaikhlbarindenisdeveloperslife.data.db.LikedPostsDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun providePostDao(database: LikedPostsDatabase) = database.postDao()

    @Provides
    @Singleton
    fun provideLikedPostsDatabase(@ApplicationContext context : Context) : LikedPostsDatabase =
        Room.databaseBuilder(context, LikedPostsDatabase::class.java, "liked_posts")
            .fallbackToDestructiveMigration()
            .build()
}
