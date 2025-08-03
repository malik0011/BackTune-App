package com.malikstudios.backtune.di

import android.content.Context
import com.malikstudios.backtune.db.YoutubeDatabase
import com.malikstudios.backtune.db.dao.YouTubeDao
import com.malikstudios.backtune.repository.YouTubeDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): YoutubeDatabase {
        return YoutubeDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideYouTubeDao(database: YoutubeDatabase): YouTubeDao {
        return database.youtubeDao()
    }

    @Provides
    @Singleton
    fun provideYouTubeDataRepository(dao: YouTubeDao): YouTubeDataRepository {
        return YouTubeDataRepository(dao)
    }
} 