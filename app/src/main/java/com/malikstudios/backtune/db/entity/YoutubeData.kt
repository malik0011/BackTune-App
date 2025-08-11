package com.malikstudios.backtune.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(
    tableName = "youtube_data",
    indices = [Index(value = ["videoId"], unique = true)]
)
data class YoutubeData(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val videoId: String ="",
    val title: String = "",
    val thumbnail: String = "",        // Can be URL or local path
    val createdAt: Long = System.currentTimeMillis(),          // Time added to DB (System.currentTimeMillis())
    val lastWatched: Long? = null, // Nullable if never watched
    val currentTimeStamp: String = "", // Duration in ISO 8601 format (e.g., PT2M34S)
) : java.io.Serializable