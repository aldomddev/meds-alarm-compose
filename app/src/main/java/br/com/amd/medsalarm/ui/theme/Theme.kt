package br.com.amd.medsalarm.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Blue400,
    primaryVariant = Blue400,
    secondary = Green200
)

private val LightColorPalette = lightColors(
    primary = Blue700,
    primaryVariant = Blue700,
    secondary = Green800,
    background = lightBackground,
    surface = lightSurface
)

@Composable
fun MedsAlarmTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}