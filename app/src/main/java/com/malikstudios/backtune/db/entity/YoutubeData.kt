package com.malikstudios.backtune.db.entity

data class YoutubeUrlData(
    val videoId: String ="",
    val title: String = "",
    val thumbnailUrl: String = "",
    val channelName: String = "",
    val currentTimeStamp: String? = "", // Duration in ISO 8601 format (e.g., PT2M34S)
) : java.io.Serializable