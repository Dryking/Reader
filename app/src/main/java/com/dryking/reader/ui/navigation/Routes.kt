package com.dryking.reader.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
data object LibraryRoute

@Serializable
data object BrowseRoute

@Serializable
data object SettingsRoute

enum class TopLevelDestination(val route: Any, val label: String, val icon: ImageVector) {
    Library(LibraryRoute, "Library", Icons.AutoMirrored.Filled.List),
    Browse(BrowseRoute, "Browse", Icons.Filled.Search),
    Settings(SettingsRoute, "Settings", Icons.Filled.Settings),
}
