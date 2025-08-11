package com.malikstudios.backtune.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.malikstudios.backtune.db.dao.YouTubeDao
import com.malikstudios.backtune.db.entity.YoutubeData

@Database(entities = [YoutubeData::class], version = 2, exportSchema = false)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract fun youtubeDao(): YouTubeDao

    companion object {
        @Volatile private var INSTANCE: YoutubeDatabase? = null

        // Migration from version 1 to 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // First, remove duplicate videoId entries, keeping only the most recent one
                database.execSQL("""
                    DELETE FROM youtube_data 
                    WHERE id NOT IN (
                        SELECT MAX(id) 
                        FROM youtube_data 
                        WHERE videoId != '' 
                        GROUP BY videoId
                    )
                """)
                
                // Now add unique index on videoId
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_youtube_data_videoId` ON `youtube_data` (`videoId`)")
            }
        }

        fun getInstance(context: Context): YoutubeDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    YoutubeDatabase::class.java,
                    "youtube_db"
                )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration() // For development - remove in production
                .build().also { INSTANCE = it }
            }
        }
    }
}