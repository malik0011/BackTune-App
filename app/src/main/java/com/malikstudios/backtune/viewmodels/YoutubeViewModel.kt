package com.malikstudios.backtune.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikstudios.backtune.db.entity.YoutubeData
import com.malikstudios.backtune.repository.YouTubeDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

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

    fun addYoutubeUrl(videoId: String) {
        // Video already exists, no need to add
        if (allVideos.value.any { it.videoId == videoId }) return

        // Insert new video data with placeholder title
        viewModelScope.launch {
            repo.insert(YoutubeData(
                videoId = videoId,
                title = "Test ${Random.nextInt()}", // Placeholder, should be updated with actual title
                lastWatched = System.currentTimeMillis(),
                currentTimeStamp = "0"
            ))
        }
    }
}
