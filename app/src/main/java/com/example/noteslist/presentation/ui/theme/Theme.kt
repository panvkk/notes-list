package com.example.noteslist.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = lightColorScheme(
    primary = Color(0xFF1387d7),
    onSecondary = Color(0xFF3F3B3C),
    error = Color(0xFFf1544b),
    errorContainer = Color(0xFFfcdfde),
)

@Composable
fun NotesListTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = NotesTypography,
        shapes = NotesShapes,
        content = content
    )
}