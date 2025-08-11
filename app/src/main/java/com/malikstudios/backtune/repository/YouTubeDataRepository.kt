package com.malikstudios.backtune.repository

import com.malikstudios.backtune.db.dao.YouTubeDao
import com.malikstudios.backtune.db.entity.YoutubeData
import kotlinx.coroutines.flow.Flow

class YouTubeDataRepository(private val dao: YouTubeDao) {

    val allVideos: Flow<List<YoutubeData>> = dao.getAllVideos()

    suspend fun insert(video: YoutubeData) = dao.insert(video)

    suspend fun insertAll(list: List<YoutubeData>) = dao.insertAll(list)

    suspend fun getVideoById(id: String): YoutubeData? = dao.getVideoById(id)

    suspend fun updateLastWatched(id: String, time: Long) = dao.updateLastWatched(id, time)

    suspend fun delete(video: YoutubeData) = dao.delete(video)

    suspend fun clearAll() = dao.clearAll()

    suspend fun cleanupInvalid() = dao.deleteInvalid()
}
