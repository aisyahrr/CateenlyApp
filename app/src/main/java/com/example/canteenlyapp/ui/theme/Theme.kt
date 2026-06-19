package com.example.canteenlyapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(

    primary = OrangePrimary,
    secondary = OrangeSecondary,

    background = Background,
    surface = White,

    onPrimary = White,
    onBackground = Black,
    onSurface = Black
)

@Composable
fun FoodAppTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}