package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TikRed,
    onPrimary = Color.White,
    primaryContainer = TikRed.copy(alpha = 0.2f),
    onPrimaryContainer = TikRed,
    secondary = TikCyan,
    onSecondary = Color.Black,
    secondaryContainer = TikCyan.copy(alpha = 0.2f),
    onSecondaryContainer = TikCyan,
    tertiary = TikGold,
    onTertiary = Color.Black,
    background = TikBlack,
    onBackground = TikLightText,
    surface = TikDarkSurface,
    onSurface = TikLightText,
    surfaceVariant = TikDarkElevated,
    onSurfaceVariant = TikMutedText,
    outline = TikDarkBorder,
    outlineVariant = TikDarkBorder.copy(alpha = 0.6f)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
