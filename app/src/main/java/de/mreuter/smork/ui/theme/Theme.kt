package de.mreuter.smork.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import de.mreuter.smork.R

private val DarkColorPalette = darkColors(
)

private val LightColorPalette = lightColors(
    primary = Purple1,
    onPrimary = White255,
    secondary = Purple2,
    onSecondary = White255,
    background = White229,
    onBackground = Black,
    //surface = White255,
    //onSurface = Gray,
    error = Red,
    onError = White255
)

@Composable
fun FreelancerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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