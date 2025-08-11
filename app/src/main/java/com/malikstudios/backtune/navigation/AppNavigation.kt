package com.malikstudios.backtune.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.malikstudios.backtune.screens.AboutScreen
import com.malikstudios.backtune.screens.HomeScreen
import com.malikstudios.backtune.screens.PlayerScreen
import com.malikstudios.backtune.screens.SettingScreen
import com.malikstudios.backtune.screens.SoundSelectionScreen
import com.malikstudios.backtune.utils.AppPreferences
import com.malikstudios.backtune.viewmodels.MainViewModel
import com.malikstudios.backtune.viewmodels.SharedIntentViewModel
import com.malikstudios.backtune.viewmodels.YouTubeViewModel

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Player : Screen("player/{videoId}") {
        fun createRoute(videoId: String) = "player/$videoId"
    }
    data object SoundSelection : Screen("sound_selection")
    data object Settings : Screen("settings")
    data object About : Screen("about")
}

/**
 * Main navigation component that handles navigation between screens
 */
@Composable
fun AppNavigation(
    paddingValues: PaddingValues,
    navController: NavHostController = rememberNavController(),
    sharedIntentViewModel: SharedIntentViewModel = hiltViewModel()
) {
    val sharedVideoId by sharedIntentViewModel.sharedVideoId.collectAsState()
    val creatorImageUrl by sharedIntentViewModel.creatorImageUrl.collectAsState()
    val appShareableURL by sharedIntentViewModel.appShareableURL.collectAsState()
    val youTubeViewModel : YouTubeViewModel = hiltViewModel()
    val allVideos by youTubeViewModel.allVideos.collectAsState()

    LaunchedEffect(allVideos) {
        allVideos.forEach { video ->
            // Update watch time for each video
            Log.d("testAyanDb", "AppNavigation-data: $video videos loaded")
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                paddingValues = paddingValues,
                previousUrlList = allVideos,
                onNavigateToPlayer = { videoId ->
                    //store the url for next session
                    AppPreferences.previousSavedYtUrl = videoId
                    navController.navigate(Screen.Player.createRoute(videoId))
                },
                onNavigateToAbout = { navController.navigate(Screen.About.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Player.route) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")
            val viewModel: MainViewModel = hiltViewModel()
            val selectedSound by viewModel.selectedSound.collectAsStateWithLifecycle()
            val volume by viewModel.volume.collectAsStateWithLifecycle()
            val isBackgroundPlaying by viewModel.isBackgroundPlaying.collectAsStateWithLifecycle()
            val isSoundSelectionVisible by viewModel.isSoundSelectionVisible.collectAsStateWithLifecycle()
            val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
            val isBlubOn by viewModel.isBlubOn.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                Log.d("testAyan", "AppNavigation: ${videoId} videoId received in PlayerScreen")
                videoId?.let {
                    youTubeViewModel.addYoutubeUrl(it) // Update watch time for the video
                    // Save the current video ID for next session
                    AppPreferences.previousSavedYtUrl = it
                }
            }

            if (videoId != null) {
                PlayerScreen(
                    videoId = videoId,
                    onNavigateBack = {
                        AppPreferences.previousSavedYtUrl = null // Clear the saved URL when navigating back
                        navController.popBackStack()
                    },
                    selectedSound = selectedSound,
                    volume = volume,
                    isBackgroundPlaying = isBackgroundPlaying,
                    isSoundSelectionVisible = isSoundSelectionVisible,
                    isLoading = isLoading,
                    isBlubOn = isBlubOn,
                    availableSounds = viewModel.availableSounds,
                    toggleBackgroundPlayback = { viewModel.toggleBackgroundPlayback() },
                    showSoundSelection = { viewModel.showSoundSelection() },
                    updateVolume = { newVolume -> viewModel.updateVolume(newVolume) },
                    hideSoundSelection = { viewModel.hideSoundSelection() },
                    selectSound = { sound -> viewModel.selectSound(sound) },
                    toggleBlubIcon = { viewModel.toggleBlubIcon() }
                )
            }
        }

        composable(Screen.SoundSelection.route) {
            val viewModel: MainViewModel = hiltViewModel()
            val selectedSound by viewModel.selectedSound.collectAsState()
            
            SoundSelectionScreen(
                sounds = viewModel.availableSounds,
                selectedSoundId = selectedSound?.id,
                onSelect = { sound ->
                    viewModel.selectSound(sound)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.About.route) {
            val uriHandler = LocalUriHandler.current
            AboutScreen(
                creatorImageUrl = creatorImageUrl,
                appShareableURL = appShareableURL,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContactMe = {
                    uriHandler.openUri("https://www.linkedin.com/in/ayan-malik-1302a3199/")
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }

    // Handle shared intents
    LaunchedEffect(sharedVideoId) {
        if (sharedVideoId != null) {
            navController.navigate(Screen.Player.createRoute(sharedVideoId!!))
            sharedIntentViewModel.clear()
        }
    }
} 
