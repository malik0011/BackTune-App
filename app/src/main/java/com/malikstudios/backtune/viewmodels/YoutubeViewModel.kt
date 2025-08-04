package com.malikstudios.backtune.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.malikstudios.backtune.db.entity.YoutubeData
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

    fun updateWatchTime(id: String) {
        viewModelScope.launch {
            repo.updateLastWatched(id, System.currentTimeMillis())
        }
    }

    fun addYoutubeUrl(videoId: String) = viewModelScope.launch {
        // Video already exists, no need to add
        if (!allVideos.value.any { it.videoId == videoId }) {
            // Fetch video info from YouTube API
            val data = viewModelScope.async {
                getYoutubeUrlInfo(videoId)
            }
            val ytUrlInfo = data.await()
            Log.d("testAyan", "addYoutubeUrl: $ytUrlInfo")
            //update the data to db
            ytUrlInfo?.let {
                repo.insert(YoutubeData(
                    videoId = it.videoId,
                    title = it.title, // Placeholder, should be updated with actual title
                    lastWatched = System.currentTimeMillis(),
                    currentTimeStamp = "0"
                ))
            }
        }
        else {
            Log.d("testAyan", "addYoutubeUrl: Video already exists, updating watch time for $videoId")
            updateWatchTime(videoId)
        }
    }

    private suspend fun getYoutubeUrlInfo(videoId: String): YoutubeData? = withContext(Dispatchers.IO) {
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
                    return@withContext parseJsonToYoutubeData(json)
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



    private fun parseJsonToYoutubeData(json: String): YoutubeData {
        // Implement your JSON parsing logic here
        // This is a placeholder implementation
        //cover json to obj using gson
        // You can use Gson or any other JSON library to parse the JSON string
        // For example, using Gson:
         val gson = Gson()
         return gson.fromJson(json, YoutubeData::class.java)
    }
}
