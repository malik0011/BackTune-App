package com.malikstudios.backtune.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.malikstudios.backtune.ui.theme.BackTuneColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onAboutClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "BackTune",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive,
                    color = BackTuneColors.Primary,
                    fontSize = 24.sp
                )
            )
        },
        actions = {
            IconButton(onClick = onAboutClick) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "About",
                    tint = BackTuneColors.TextSecondary
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(id = com.malikstudios.backtune.R.drawable.ic_settings), // create this icon
                    contentDescription = "Settings",
                    tint = BackTuneColors.TextSecondary
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarPreview() {
    Column(Modifier.background(BackTuneColors.Background)) {
        HomeTopBar(
            onAboutClick = {},
            onSettingsClick = {}
        )
    }
}

