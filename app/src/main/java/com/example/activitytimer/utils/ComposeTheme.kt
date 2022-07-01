package com.example.activitytimer.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

object ComposeTheme {
    val Beige = Color(0xFFFEFFDE)
    val LightGreen = Color(0xffDDFFBC)
    val Green = Color(0xff91C788)
    val DarkGreen = Color(0xff52734D)
    val Teal = Color(0xff00C897)
    val GreenTeal = Color(0xff019267)

    val DarkColors = darkColors(
        primary = DarkGreen,
        secondary = Teal,
        primaryVariant = DarkGreen,
        secondaryVariant = GreenTeal,
        onPrimary = Color.White,
        onSecondary = DarkGreen,
        background = Color.Black,
    )
    val LightColors = lightColors(
        primary = Green,
        secondary = Teal,
        primaryVariant = DarkGreen,
        secondaryVariant = GreenTeal,
        onPrimary = Color.White,
        onSecondary = DarkGreen,
        background = Beige
    )
}