package com.malikstudios.backtune.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class YoutubeInfo(
    var title: String = "",
    @SerializedName("author_name") var authorName: String = "",
    @SerializedName("author_url") var authorUrl: String = "",
    var type: String = "",
    var height: Int = 0,
    var width: Int = 0,
    var version: String = "",
    @SerializedName("provider_name") var providerName: String = "",
    @SerializedName("provider_url") var providerUrl: String = "",
    @SerializedName("thumbnail_height") var thumbnailHeight: Int = 0,
    @SerializedName("thumbnail_width") var thumbnailWidth: Int = 0,
    @SerializedName("thumbnail_url") var thumbnailUrl: String = "",
    var html: String = ""
)