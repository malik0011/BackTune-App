package com.malikstudios.backtune.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malikstudios.backtune.R
import com.malikstudios.backtune.db.entity.YoutubeData
import com.malikstudios.backtune.screens.home.HomeTopBar
import com.malikstudios.backtune.screens.home.PreviousVideoTicketCard2
import com.malikstudios.backtune.ui.theme.BackTuneColors
import com.malikstudios.backtune.ui.theme.BackTuneTheme
import com.malikstudios.backtune.utils.Constants

/**
 * Home screen of the app where users can enter a YouTube URL
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    previousUrlList: List<YoutubeData>,
    onNavigateToPlayer: (String) -> Unit,
    onNavigateToAbout: () -> Unit
) {
    var videoUrl by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        Modifier
        .fillMaxSize()
        .background(BackTuneColors.Background)
        .verticalScroll(rememberScrollState())
    ) {
        HomeTopBar(
            onAboutClick = onNavigateToAbout,
            onSettingsClick = { /* TODO: Implement settings navigation */ }
        )
        Column(
            modifier = Modifier
                //.fillMaxSize()
                .background(BackTuneColors.Background)
                .padding(12.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo and Title Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_backtune_logo),
                    contentDescription = "BackTune Logo",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = stringResource(R.string.home_subtitle),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp
                    ),
                    color = BackTuneColors.TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // URL Input Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = BackTuneColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.youtube_url_hint),
                        style = MaterialTheme.typography.titleMedium,
                        color = BackTuneColors.TextPrimary
                    )

                    OutlinedTextField(
                        value = videoUrl,
                        onValueChange = {
                            videoUrl = it
                            showError = false
                        },
                        isError = showError,
                        supportingText = {
                            if (showError) {
                                Text(
                                    text = stringResource(R.string.error_invalid_url),
                                    color = BackTuneColors.AccentRed
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = BackTuneColors.Primary,
                            unfocusedBorderColor = BackTuneColors.TextTertiary,
                            cursorColor = BackTuneColors.Primary,
                            focusedLabelColor = BackTuneColors.Primary,
                            unfocusedLabelColor = BackTuneColors.TextSecondary,
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = BackTuneColors.TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))



            // Play Button
            Button(
                onClick = {
                    if (videoUrl.matches(Regex(Constants.YOUTUBE_URL_PATTERN))) {
                        // Extract video ID from URL
                        val videoId = extractVideoId(videoUrl)
                        if (videoId != null) {
                            onNavigateToPlayer(videoId)
                        } else {
                            showError = true
                        }
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BackTuneColors.Primary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.play_button),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            HorizontalDivider(
                Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
                    .scale(scaleX = 1f, scaleY =  1f),
            )

            Text(
                text = stringResource(R.string.previous_videos),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = BackTuneColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )

            val validVideos = previousUrlList.filter { it.videoId.isNotBlank() }

            if (validVideos.isEmpty()) {
                Text(
                    text = "No history yet. Paste a YouTube link above to get started!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = BackTuneColors.TextSecondary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                validVideos.forEach { video ->
                    PreviousVideoTicketCard2(
                        video = video,
                        onClick = {
                            if (video.videoId.isNotBlank()) {
                                onNavigateToPlayer(video.videoId)
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * Extracts video ID from YouTube URL
 * @param url YouTube URL
 * @return Video ID or null if invalid
 */
private fun extractVideoId(url: String): String? {
    val pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"
    val matcher = java.util.regex.Pattern.compile(pattern).matcher(url)
    return if (matcher.find()) matcher.group() else null
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BackTuneTheme {
        HomeScreen(
            paddingValues = PaddingValues(16.dp),
            previousUrlList = listOf(
                YoutubeData(videoId = "dQw4w9WgXcQ", title = "Sample Video", thumbnail = "https://img.youtube.com/vi/dQw4w9WgXcQ/0.jpg")
            ),
            onNavigateToPlayer = {},
            onNavigateToAbout = {}
        )
    }
}