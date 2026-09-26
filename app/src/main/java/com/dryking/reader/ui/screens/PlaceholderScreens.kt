package com.dryking.reader.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dryking.reader.BuildConfig
import com.dryking.reader.ui.theme.ReaderTheme

// Temporary screens for the skeleton; each is replaced by its real feature in later milestones.

@Composable
fun LibraryScreen() = Placeholder("Library", "Saved works will appear here.")

@Composable
fun BrowseScreen() = Placeholder("Browse", "Search and tag browsing are coming soon.")

@Composable
fun SettingsScreen() = Placeholder("Settings", "Version ${BuildConfig.VERSION_NAME}")

@Composable
private fun Placeholder(title: String, message: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceholderPreview() {
    ReaderTheme { LibraryScreen() }
}
