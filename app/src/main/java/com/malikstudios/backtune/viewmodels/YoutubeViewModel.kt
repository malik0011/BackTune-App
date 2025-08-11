package com.malikstudios.backtune.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.malikstudios.backtune.db.entity.YoutubeData
import com.malikstudios.backtune.models.YoutubeInfo
import com.malikstudios.backtune.repository.YouTubeDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class YouTubeViewModel @Inject constructor(
    private val repo: YouTubeDataRepository
) : ViewModel() {

    val allVideos = repo.allVideos.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val recentVideos = repo.recentVideos.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun updateWatchTime(id: String) {
        viewModelScope.launch {
            repo.updateLastWatched(id, System.currentTimeMillis())
        }
    }

    fun addYoutubeUrl(videoId: String) = viewModelScope.launch {
        if (videoId.isBlank()) {
            Log.w("testAyan", "addYoutubeUrl: Empty videoId provided, skipping")
            return@launch
        }
        
        val now = System.currentTimeMillis()
        // Cleanup any invalid rows opportunistically
        repo.cleanupInvalid()
        
        // Check if video already exists in current list
        val existingVideo = allVideos.value.firstOrNull { it.videoId == videoId }
        
        if (existingVideo != null) {
            // Video already exists, just update the last watched time
            Log.d("testAyan", "addYoutubeUrl: Video already exists, updating watch time for $videoId")
            repo.updateLastWatched(existingVideo.id, now)
        } else {
            // Video doesn't exist, fetch info and insert
            Log.d("testAyan", "addYoutubeUrl: New video, fetching info for $videoId")
            val info = getYoutubeUrlInfo(videoId)
            
            if (info != null) {
                try {
                    repo.insert(
                        YoutubeData(
                            videoId = videoId,
                            title = info.title.ifBlank { "YouTube Video" },
                            lastWatched = now,
                            currentTimeStamp = "0"
                        )
                    )
                    Log.d("testAyan", "addYoutubeUrl: Successfully inserted video $videoId")
                } catch (e: Exception) {
                    Log.e("testAyan", "addYoutubeUrl: Failed to insert video $videoId", e)
                }
            } else {
                Log.w("testAyan", "addYoutubeUrl: Failed to fetch video info for $videoId")
            }
        }
    }

    private suspend fun getYoutubeUrlInfo(videoId: String): YoutubeInfo? = withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://www.youtube.com/oembed?url=https://youtu.be/$videoId&format=json")
                .addHeader(
                    "Cookie",
                    "GPS=1; VISITOR_INFO1_LIVE=KjaR5we8FZ4; VISITOR_PRIVACY_METADATA=CgJVUxIEGgAgLg%3D%3D; YSC=W6gRtA4e_is; __Secure-ROLLOUT_TOKEN=CITphfvQ16LbgAEQsq7o4-vijgMY5I3xx6jvjgM%3D"
                )
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                response.body?.string()?.let { json ->
                    return@withContext parseJsonToYoutubeInfo(json)
                }
            }

            return@withContext null
        } catch (e: IOException) {
            // Network-related exception (e.g., timeout, no connection)
            e.printStackTrace()
            return@withContext null
        } catch (e: Exception) {
            // JSON parsing or other unexpected errors
            e.printStackTrace()
            return@withContext null
        }
    }



    private fun parseJsonToYoutubeInfo(json: String): YoutubeInfo {
        val gson = Gson()
        return gson.fromJson(json, YoutubeInfo::class.java)
    }
}
