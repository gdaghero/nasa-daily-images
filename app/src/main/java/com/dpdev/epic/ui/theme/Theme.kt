package com.dpdev.epic.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Denim,
    secondary = RegentStBlue,
    background = Bunker,
    surface = Bunker,
    onPrimary = White,
    onSecondary = CodGray,
    onTertiary = CodGray,
    onBackground = White,
    onSurface = White,
    onSurfaceVariant = White,
    surfaceVariant = EbonyClay
)

@Composable
fun EpicTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
