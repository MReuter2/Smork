package de.mreuter.freelancer.ui.theme

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val BackgroundColor = Color(200, 200, 200, 255)

val Black07 = Color(7, 7, 7, 204)
val Gray14 = Color(14, 14, 14, 153)
val Purple = Color(120,121,241)

val White = Color(255, 255, 255, 204)

val LinkBlue = Color(8, 62, 203, 153)

val ButtonBackgroundColor = Purple
val ButtonContentColor = White

@Composable
fun defaultTextFieldColors() =
    TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = Purple,
        cursorColor = Gray14,
        focusedLabelColor = Black07,
        unfocusedLabelColor = Black07
    )