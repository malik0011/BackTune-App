package com.malikstudios.backtune.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikstudios.backtune.repository.YouTubeDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: YouTubeDataRepository
) : ViewModel() {

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }
}


