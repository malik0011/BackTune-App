package com.malikstudios.backtune.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.malikstudios.backtune.R
import com.malikstudios.backtune.db.entity.YoutubeData
import com.malikstudios.backtune.ui.theme.BackTuneColors
import com.malikstudios.backtune.ui.theme.BackTuneTheme
import com.malikstudios.backtune.utils.TimeUtils

@Composable
fun PreviousVideoTicketCard2(
    video: YoutubeData,
    onClick: () -> Unit
) {
    val thumbnailUrl = "https://img.youtube.com/vi/${video.videoId}/hqdefault.jpg"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = BackTuneColors.CardBackground
        ),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Thumbnail",
                placeholder = painterResource(id = R.drawable.ic_backtune_logo),
                error = painterResource(id = R.drawable.ic_backtune_logo),
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (video.title.isNotBlank()) video.title else "YouTube Video",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = BackTuneColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Last watched: ${TimeUtils.getRelativeTime(video.lastWatched ?: 0L)}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = BackTuneColors.TextSecondary
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "https://youtu.be/${video.videoId}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = BackTuneColors.TextTertiary
                )
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun PreviousVideoCardPreview() {
    BackTuneTheme {
        Column(Modifier.background(BackTuneColors.Background)) {
            PreviousVideoTicketCard2(
                video = YoutubeData(
                    videoId = "MXDOderMPq4",
                    title = "Sunday Suspense | ostitto | Sunday Suspense Horror Special | Mirchi 98.3 | MirAfsarAli",
                    lastWatched = System.currentTimeMillis(),
                    currentTimeStamp = "0",
                    createdAt = System.currentTimeMillis()
                )
            ) {
                // Handle click
            }
        }
    }
}
