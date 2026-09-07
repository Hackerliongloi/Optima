package com.iqoo.aiphonedoctor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = IqooOrange,
    onPrimary = CyberBlack,
    primaryContainer = DarkCardBg,
    onPrimaryContainer = TextPrimary,
    secondary = AccentCyan,
    onSecondary = CyberBlack,
    background = CyberBlack,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCardBg,
    onSurfaceVariant = TextSecondary,
    outline = DarkCardBorder,
    error = AlertRed,
    onError = TextPrimary
)

@Composable
fun AIPhoneDoctorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
