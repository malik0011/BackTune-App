package com.malikstudios.backtune.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.malikstudios.backtune.db.entity.YoutubeData
import com.malikstudios.backtune.ui.theme.BackTuneColors
import com.malikstudios.backtune.ui.theme.BackTuneTheme
import com.malikstudios.backtune.utils.TimeUtils

@Composable
fun PreviousVideoTicketCard2(
    video: YoutubeData,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 2.dp,
                brush = SolidColor(MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(16.dp)
            )
            .drawBehind {
                val stroke = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f))
                )
                drawRoundRect(
                    color = Color.Blue,
                    style = stroke,
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontFamily = FontFamily.SansSerif
                ),
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Last watched: ${TimeUtils.getRelativeTime(video.lastWatched?: 0L)}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Serif
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

//            Text(
//                text = "Created At: ${TimeUtils.getRelativeTime(video.createdAt ?: 0L)}",
//                style = MaterialTheme.typography.bodySmall.copy(
//                    fontSize = 8.sp,
//                    fontFamily = FontFamily.Serif
//                ),
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                Text(
                    text = "🎞 ",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace
                    )
                )
                Text(
                    text = "https://www.youtube.com/watch?v=${video.videoId}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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
