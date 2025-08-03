package com.malikstudios.backtune.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.malikstudios.backtune.db.dao.YouTubeDao
import com.malikstudios.backtune.db.entity.YoutubeData

@Database(entities = [YoutubeData::class], version = 1, exportSchema = false)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract fun youtubeDao(): YouTubeDao

    companion object {
        @Volatile private var INSTANCE: YoutubeDatabase? = null

        fun getInstance(context: Context): YoutubeDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    YoutubeDatabase::class.java,
                    "youtube_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}