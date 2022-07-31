package de.mreuter.smork.ui.screens.company

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import de.mreuter.smork.R
import de.mreuter.smork.backend.core.Address
import de.mreuter.smork.backend.core.EmailAddress
import de.mreuter.smork.backend.core.Fullname
import de.mreuter.smork.backend.worker.domain.Worker
import de.mreuter.smork.ui.elements.BasicLazyColumn
import de.mreuter.smork.ui.elements.BasicOutlinedTextField
import de.mreuter.smork.ui.elements.BasicScaffold
import de.mreuter.smork.ui.elements.PrimaryButton
import java.util.*

@Composable
fun WorkerCreating(
    onWorkerSave: (Worker) -> Unit,
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
){
    val firstnameValue = remember { mutableStateOf(String()) }
    val lastnameValue = remember { mutableStateOf(String()) }
    val emailValue = remember { mutableStateOf(String()) }
    val postcodeValue =
        remember { mutableStateOf(String()) }
    val cityValue = remember { mutableStateOf(String()) }
    val streetValue = remember { mutableStateOf(String()) }
    val housenumberValue =
        remember { mutableStateOf(String()) }
    val phoneValue = remember { mutableStateOf(String()) }

    val isError = remember { mutableStateOf(false) }

    val context = LocalContext.current

    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "Worker Creating",
        backNavigation = { backNavigation() }) {
        BasicLazyColumn {
            Spacer(modifier = Modifier.padding(5.dp))
            BasicOutlinedTextField(
                label = stringResource(id = R.string.firstname),
                value = firstnameValue.value,
                onValueChange = { firstnameValue.value = it },
                isError = isError.value && firstnameValue.value.trim() == ""
            )
            Spacer(modifier = Modifier.padding(5.dp))
            BasicOutlinedTextField(
                label = stringResource(id = R.string.lastname),
                value = lastnameValue.value,
                onValueChange = { lastnameValue.value = it },
                isError = isError.value && lastnameValue.value == ""
            )
            Spacer(modifier = Modifier.padding(5.dp))
            BasicOutlinedTextField(
                label = "Email",
                value = emailValue.value,
                onValueChange = { emailValue.value = it },
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Row {
                Column(modifier = Modifier.weight(2F)) {
                    BasicOutlinedTextField(
                        label = "Postcode",
                        value = postcodeValue.value,
                        onValueChange = {
                            if (it.isDigitsOnly() && (postcodeValue.value.length < 5 || it.length <= 5))
                                postcodeValue.value = it
                        },
                        keyboardType = KeyboardType.Number
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Column(modifier = Modifier.weight(6F)) {
                    BasicOutlinedTextField(
                        label = "City",
                        value = cityValue.value,
                        onValueChange = { cityValue.value = it }
                    )
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))
            Row {
                Column(modifier = Modifier.weight(6F)) {
                    BasicOutlinedTextField(
                        label = "Street",
                        value = streetValue.value,
                        onValueChange = {
                            streetValue.value = it
                        }
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Column(modifier = Modifier.weight(2F)) {
                    BasicOutlinedTextField(
                        label = "Nr",
                        value = housenumberValue.value,
                        onValueChange = {
                            if (it.isDigitsOnly())
                                housenumberValue.value = it
                        },
                        keyboardType = KeyboardType.Number
                    )
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))
            BasicOutlinedTextField(
                label = "Phone number",
                value = phoneValue.value,
                onValueChange = {
                    if (it.isDigitsOnly())
                        phoneValue.value = it
                },
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.padding(15.dp))
            PrimaryButton(label = "Save") {
                if (firstnameValue.value.trim() != "" && lastnameValue.value.trim() != "") {
                    val address = if (
                        postcodeValue.value.toIntOrNull() != null
                        && postcodeValue.value.length == 5
                        && cityValue.value != ""
                        && streetValue.value != ""
                        && housenumberValue.value.toIntOrNull() != null
                    ) {
                        Address(
                            postcodeValue.value.toIntOrNull() ?: 0,
                            cityValue.value,
                            streetValue.value,
                            housenumberValue.value.toIntOrNull() ?: 0
                        )
                    } else {
                        null
                    }

                    val email = if (emailValue.value.contains("@")) EmailAddress(emailValue.value) else null

                    val newWorker = Worker(
                        fullname = Fullname(firstnameValue.value, lastnameValue.value),
                        phonenumber = phoneValue.value.toLongOrNull(),
                        address = address,
                        emailAddress = email
                    )
                    onWorkerSave(newWorker)
                    Toast.makeText(context, "$newWorker saved", Toast.LENGTH_LONG).show()
                } else {
                    isError.value = true
                }
            }
        }
    }
}