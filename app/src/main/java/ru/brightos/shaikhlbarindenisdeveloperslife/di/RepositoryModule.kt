package ru.brightos.shaikhlbarindenisdeveloperslife.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.brightos.shaikhlbarindenisdeveloperslife.data.repository.LikedPostsRepository
import ru.brightos.shaikhlbarindenisdeveloperslife.data.repository.LikedPostsRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindLikedPostsRepository(likedPostsRepositoryImpl: LikedPostsRepositoryImpl): LikedPostsRepository
}
