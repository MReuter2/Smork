package de.mreuter.smork.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.ui.theme.*


@Composable
fun PrimaryButton(label: String, onClick: () -> Unit) {
    OutlinedButton(
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
        ),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun SecondaryButton(label: String, onClick: () -> Unit) {
    OutlinedButton(
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Purple1
        ),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        border = BorderStroke(1.dp, Purple1),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = label,
            style = TextStyle(color = Black)
        )
    }
}

@Preview
@Composable
fun ButtonPreview(){
    SmorkTheme {
        Column {
            PrimaryButton(label = "Test Button") {

            }
            SecondaryButton(label = "Test Button") {

            }
        }
    }
}