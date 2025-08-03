package com.malikstudios.backtune.db.dao

import androidx.room.*
import com.malikstudios.backtune.db.entity.YoutubeData
import kotlinx.coroutines.flow.Flow

@Dao
interface YouTubeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(video: YoutubeData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<YoutubeData>)

    @Query("SELECT * FROM youtube_data ORDER BY createdAt DESC")
    fun getAllVideos(): Flow<List<YoutubeData>>

    @Query("SELECT * FROM youtube_data WHERE id = :id")
    suspend fun getVideoById(id: String): YoutubeData?

    @Query("UPDATE youtube_data SET lastWatched = :timestamp WHERE id = :id")
    suspend fun updateLastWatched(id: String, timestamp: Long)

    @Delete
    suspend fun delete(video: YoutubeData)

    @Query("DELETE FROM youtube_data")
    suspend fun clearAll()
}
