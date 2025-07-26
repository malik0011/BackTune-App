package com.malikstudios.backtune.screens

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.malikstudios.backtune.R
import com.malikstudios.backtune.models.AmbientSound
import com.malikstudios.backtune.ui.theme.BackTuneColors
import com.malikstudios.backtune.ui.theme.BackTuneTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    videoId: String,
    onNavigateBack: () -> Unit,
    selectedSound: AmbientSound?,
    volume: Float,
    isBackgroundPlaying: Boolean,
    isSoundSelectionVisible: Boolean,
    isLoading: Boolean,
    availableSounds : List<AmbientSound>,
    isBlubOn: Boolean = false,
    toggleBackgroundPlayback: () -> Unit,
    showSoundSelection: () -> Unit,
    updateVolume: (Float) -> Unit,
    hideSoundSelection: () -> Unit,
    selectSound: (AmbientSound) -> Unit,
    toggleBlubIcon: () -> Unit,
) {
    val context = LocalContext.current
    val animatedAlpha by animateFloatAsState(
        targetValue = if (!isBlubOn) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "OverlayAlpha"
    )

    val activity = context as? Activity
    val window = activity?.window
    val insetsController = window?.let { WindowCompat.getInsetsController(it, it.decorView) }

    // React to isBlubOn to toggle full-screen
    LaunchedEffect(isBlubOn) {
        insetsController?.let {
            if (!isBlubOn) {
                it.hide(WindowInsetsCompat.Type.systemBars())
                it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                it.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }



    var youTubePlayer by remember { mutableStateOf<YouTubePlayer?>(null) }
    var currentPlayerPosition by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 16.dp)
            .background(BackTuneColors.Background)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
//            if(!isBlubOn) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isBlubOn,
                    enter = fadeIn(animationSpec = tween(500)),
                    exit = fadeOut(animationSpec = tween(500)),
                    modifier = Modifier.fillMaxSize().zIndex(10f)
                ) {
                    Box(modifier = Modifier
//                        .zIndex(10f)
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = animatedAlpha))
                        .clickable {  }
                    ) {
                        TopAppBar(
                            title = {
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ){
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = toggleBlubIcon) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_blub_off),
                                            contentDescription = "lock you screen screen with back tune",
                                            tint = BackTuneColors.TextPrimary.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            },
                            navigationIcon = {},
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Black
                            )
                        )
                    }
                }

//            }
            Column {
                // Top Bar
                TopAppBar(
                    title = {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Text(
                                text = stringResource(R.string.player_title),
                                color = BackTuneColors.TextPrimary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = toggleBlubIcon) {
                                Icon(
                                    painter = painterResource(if (isBlubOn) R.drawable.ic_blub_on else R.drawable.ic_blub_off),
                                    contentDescription = "lock you screen screen with back tune",
                                    tint = BackTuneColors.TextPrimary
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = BackTuneColors.TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BackTuneColors.Surface
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                ) {
                    // YouTube Player
                    AndroidView(
                        factory = { context ->
                            YouTubePlayerView(context).apply {
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                                )
                                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                    override fun onReady(player: YouTubePlayer) {
                                        youTubePlayer = player
                                        player.loadVideo(videoId, currentPlayerPosition)
                                    }

                                    override fun onCurrentSecond(
                                        youTubePlayer: YouTubePlayer,
                                        second: Float
                                    ) {
                                        super.onCurrentSecond(youTubePlayer, second)
                                        currentPlayerPosition = second
                                    }

                                    override fun onStateChange(
                                        youTubePlayer: YouTubePlayer,
                                        state: PlayerConstants.PlayerState
                                    ) {
                                        super.onStateChange(youTubePlayer, state)
                                        when (state) {
                                            PlayerConstants.PlayerState.ENDED -> {
                                                // Stop background music when video ends
                                                toggleBackgroundPlayback()
                                            }

                                            PlayerConstants.PlayerState.PAUSED -> {
                                                // Pause background music when video is paused
                                                if (isBackgroundPlaying) {
                                                    toggleBackgroundPlayback()
                                                }
                                            }

                                            PlayerConstants.PlayerState.PLAYING -> {
                                                // Resume background music when video starts playing
                                                if (!isBackgroundPlaying && selectedSound != null) {
                                                    toggleBackgroundPlayback()
                                                }
                                            }

                                            else -> {}
                                        }
                                    }
                                })
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Background Sound Controls
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = BackTuneColors.CardBackground
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.background_sound),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = BackTuneColors.TextPrimary
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (selectedSound != null) {
                                        IconButton(
                                            onClick = { toggleBackgroundPlayback() }
                                        ) {
                                            Icon(
                                                painter = painterResource(
                                                    id =
                                                        if (isBackgroundPlaying)
                                                            R.drawable.ic_pause
                                                        else
                                                            R.drawable.ic_play
                                                ),
                                                contentDescription = if (isBackgroundPlaying)
                                                    "Pause Background"
                                                else
                                                    "Play Background",
                                                tint = BackTuneColors.Primary
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = { showSoundSelection() },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = BackTuneColors.Primary
                                        ),
                                        enabled = !isLoading
                                    ) {
                                        if (isLoading) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                color = BackTuneColors.TextPrimary
                                            )
                                        } else {
                                            Text(stringResource(R.string.select_sound))
                                        }
                                    }
                                }
                            }

                            if (selectedSound != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = selectedSound?.name ?: "",
                                    color = BackTuneColors.TextSecondary
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_volume_down),
                                        contentDescription = "Volume",
                                        tint = BackTuneColors.TextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Slider(
                                        value = volume,
                                        onValueChange = { updateVolume(it) },
                                        colors = SliderDefaults.colors(
                                            thumbColor = BackTuneColors.Primary,
                                            activeTrackColor = BackTuneColors.Primary,
                                            inactiveTrackColor = BackTuneColors.TextTertiary
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_volume_up),
                                        contentDescription = "Volume",
                                        tint = BackTuneColors.TextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    text = "Background Music Volume",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BackTuneColors.TextSecondary,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }

                    // Sound Selection Bottom Sheet
                    if (isSoundSelectionVisible) {
                        ModalBottomSheet(
                            onDismissRequest = { hideSoundSelection() },
                            containerColor = BackTuneColors.Surface
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.sound_selection_title),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = BackTuneColors.TextPrimary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(availableSounds) { sound ->
                                        SoundItem(
                                            sound = sound,
                                            isSelected = sound.id == selectedSound?.id,
                                            onSelect = { selectSound(sound) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Add the tips card at the bottom
                    Spacer(modifier = Modifier.weight(1f))
                    UserTipsCard()
                }
            }
        }
    }
}

@Composable
fun SoundItem(
    sound: AmbientSound,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val icon = when (sound.id) {
        "rain" -> R.drawable.ic_rain
        "long_rain" -> R.drawable.ic_long_rain
        "waves" -> R.drawable.ic_waves
        "forest" -> R.drawable.ic_forest
        else -> R.drawable.ic_music
    }

    val backgroundColor = if (isSelected) {
        BackTuneColors.Primary.copy(alpha = 0.1f)
    } else {
        BackTuneColors.CardBackground
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) BackTuneColors.Primary
                        else BackTuneColors.Primary.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = sound.name,
                    colorFilter = ColorFilter.tint(if (isSelected) Color.White else BackTuneColors.Primary),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Sound Info
            Text(
                text = sound.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = BackTuneColors.TextPrimary
            )

            // Selection Indicator
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = BackTuneColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun UserTipsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackTuneColors.CardBackground.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Tips",
                    tint = BackTuneColors.Primary
                )
                Text(
                    text = "Tips for Best Experience",
                    style = MaterialTheme.typography.titleMedium,
                    color = BackTuneColors.TextPrimary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Tip items
            TipItem(
                icon = R.drawable.ic_headphone,
                text = "Use headphones for the best audio experience"
            )
            TipItem(
                icon = R.drawable.ic_volume_up,
                text = "Adjust background sound volume to your preference"
            )
            TipItem(
                icon = R.drawable.ic_lightbulb,
                text = "Try different sounds to find your perfect mix"
            )
            TipItem(
                icon = R.drawable.ic_blub_off,
                text = "Use top right icon to Lock the screen & keep the music playing while screen is off"
            )
        }
    }
}

@Composable
fun TipItem(
    icon: Int,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(BackTuneColors.TextSecondary),
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = BackTuneColors.TextSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerScreenPreview() {
    BackTuneTheme {
        PlayerScreen(
            videoId = "dQw4w9WgXcQ",
            onNavigateBack = {},
            selectedSound = AmbientSound("rain", "Rain", "rain"),
            volume = 0.5f,
            isBackgroundPlaying = true,
            isSoundSelectionVisible = false,
            isLoading = false,
            availableSounds = listOf(
                AmbientSound("rain", "Rain", "rain"),
                AmbientSound("long_rain", "Long Rain", "long_rain"),
                AmbientSound("waves", "Waves", "waves"),
                AmbientSound("forest", "Forest", "forest")
            ),
            toggleBackgroundPlayback = {},
            showSoundSelection = {},
            updateVolume = {},
            hideSoundSelection = {},
            selectSound = {},
            toggleBlubIcon = { /* No-op for preview */ },
        )
    }
} 