package de.mreuter.smork.ui.elements

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import de.mreuter.smork.ui.theme.SmorkTheme

@Composable
fun DeleteDialog(openDeleteDialog: MutableState<Boolean>, onProjectDelete: () -> Unit){
    AlertDialog(
        onDismissRequest = {
            openDeleteDialog.value = false
        },
        title = {
            Text(text = "Are you sure?")
        },
        dismissButton = {
            TextButton(
                onClick = { openDeleteDialog.value = false }
            ){
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onProjectDelete() }
            ){
                Text(text = "Confirm ")
            }
        }
    )
}

@Preview
@Composable
fun DeleteDialogPreview(){
    SmorkTheme {
        DeleteDialog(openDeleteDialog = remember{ mutableStateOf(true) }) {}
    }
}