package de.mreuter.smork.ui.theme

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource


val Purple1 = Color(93,95,239)
val Purple2 = Color(120,121,241)

val Black = Color(7, 7, 7, 204)
val Gray = Color(20, 20, 20, 153)
val White255 = Color(255,255,255,204)
val White229 = Color(246, 246, 246, 255)
val Red = Color(255,65,65,204)

val LinkBlue = Color(8, 62, 203, 153)

@Composable
fun defaultTextFieldColors() =
    TextFieldDefaults.outlinedTextFieldColors(
        textColor = Black,
        disabledTextColor = Black,
        focusedBorderColor = Purple1,
        disabledBorderColor = Black,
        cursorColor = Purple1,
        focusedLabelColor = Black,
        unfocusedLabelColor = Black,
        disabledLabelColor = Black,
        backgroundColor = White255,
        errorTrailingIconColor = Red,
        errorBorderColor = Red,
        errorCursorColor = Red,
        errorLabelColor = Red,
        errorLeadingIconColor = Red
    )