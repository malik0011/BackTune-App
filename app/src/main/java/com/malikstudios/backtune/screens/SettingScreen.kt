package com.malikstudios.backtune.screens

import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.malikstudios.backtune.ui.theme.BackTuneColors
import com.malikstudios.backtune.utils.AppPreferences
import com.malikstudios.backtune.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var showPrevious by remember { mutableStateOf(AppPreferences.showPreviousVideos) }
    var selectedLock by remember { mutableStateOf(AppPreferences.lockScreenType) }

    val versionName = try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        pInfo.versionName ?: ""
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = BackTuneColors.TextPrimary
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "General",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BackTuneColors.TextPrimary
                ),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            ListItem(
                headlineContent = { Text("Show previous videos") },
                supportingContent = { Text("Toggle history list on the Home screen") },
                trailingContent = {
                    Switch(
                        checked = showPrevious,
                        onCheckedChange = { checked ->
                            showPrevious = checked
                            AppPreferences.showPreviousVideos = checked
                        }
                    )
                }
            )

            Divider()

            ListItem(
                headlineContent = { Text("Lock screen type") },
                supportingContent = { Text("Coming soon") },
                trailingContent = {
                    Text(
                        text = when (selectedLock) {
                            "none" -> "None"
                            else -> "None"
                        },
                        color = BackTuneColors.TextSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            ListItem(
                headlineContent = { Text("App version") },
                supportingContent = { Text(versionName) }
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "More",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BackTuneColors.TextPrimary
                ),
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            var showConfirm by remember { mutableStateOf(false) }

            ListItem(
                modifier = Modifier.fillMaxWidth(),
                headlineContent = { Text("Clear history") },
                supportingContent = { Text("Remove all saved previous videos") },
                trailingContent = {
                    Text(
                        text = "Clear",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { showConfirm = true }
                    )
                }
            )

            if (showConfirm) {
                AlertDialog(
                    onDismissRequest = { showConfirm = false },
                    confirmButton = {
                        Text(
                            text = "Delete",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.clickable {
                                showConfirm = false
                                viewModel.clearAllHistory()
                            }
                        )
                    },
                    dismissButton = {
                        Text(
                            text = "Cancel",
                            modifier = Modifier.clickable { showConfirm = false }
                        )
                    },
                    title = { Text("Clear all history?") },
                    text = { Text("This will remove all previously saved videos.") }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    SettingScreen(
        onNavigateBack = {}
    )
}