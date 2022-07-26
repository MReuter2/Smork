package de.mreuter.smork.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import de.mreuter.smork.R
import de.mreuter.smork.backend.*
import de.mreuter.smork.backend.client.application.ClientEntity
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.core.Address
import de.mreuter.smork.backend.core.EmailAddress
import de.mreuter.smork.backend.core.Fullname
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.exampleClients
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.theme.*
import java.util.*


@Composable
fun Clients(
    navigateToClient: (Client) -> Unit = {},
    navigateToNewClient: () -> Unit = {},
    clients: List<Client>?,
    bottomBar: @Composable () -> Unit
) {

    val sortedClients = clients
                ?.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "Clients",
        trailingAppBarIcons = {
            IconButton(
                onClick = {
                    navigateToNewClient()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_add_24),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ) {
        BasicLazyColumn {
            var lastFirstLetter = '-'
            var currentList = mutableListOf<Client>()
            val clientsDividedByFirstLetter = mutableListOf<List<Client>>()
            val firstletters = mutableListOf<String>()
            sortedClients?.forEach { client ->
                val currentFirstLetter = client.fullname.lastname[0]
                if (currentFirstLetter == lastFirstLetter) {
                    currentList.add(client)
                } else
                    if (currentFirstLetter != lastFirstLetter) {
                        firstletters.add(currentFirstLetter.uppercase())
                        if (currentList.isNotEmpty())
                            clientsDividedByFirstLetter.add(currentList)
                        lastFirstLetter = currentFirstLetter
                        currentList = mutableListOf(client)
                    }
                if (sortedClients.last() == client && currentList.isNotEmpty())
                    clientsDividedByFirstLetter.add(currentList)
            }
            firstletters.forEach { letter ->
                Text(
                    text = letter,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(top = 15.dp)
                )
                BasicCard {
                    clientsDividedByFirstLetter[firstletters.indexOf(letter)].forEach { client ->
                        if (client.fullname.lastname[0].uppercase() == letter) {
                            ClickableListItem(
                                subtitle = client.fullname.toString(),
                                action = {
                                    navigateToClient(client)
                                }
                            )
                        }
                        if (client != clientsDividedByFirstLetter[firstletters.indexOf(letter)].last()
                        ) {
                            BasicDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClientView(
    client: Client,
    navigateToEditView: (Client) -> Unit = {},
    navigateToNewProject: (Client) -> Unit = {},
    navigateToProject: (Project) -> Unit = {},
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit = {}
) {
    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "${client.fullname}",
        backNavigation = backNavigation,
        trailingAppBarIcons = {
            IconButton(onClick = { navigateToEditView(client) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_edit_24),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ) {
        BasicLazyColumn {
            BasicCard {
                BasicListItem(
                    topic = "Phone number",
                    description = client.phonenumber?.toString() ?: ""
                )
                BasicDivider()
                Spacer(modifier = Modifier.padding(15.dp))
                BasicListItem(topic = "Email", description = client.emailAddress?.toString() ?: "")
                BasicDivider()
                Spacer(modifier = Modifier.padding(15.dp))
                BasicListItem(topic = "Address", description = client.address?.toString() ?: "")
                BasicDivider()
            }
            BasicCard {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Next maintenances",
                        style = MaterialTheme.typography.h2
                    )
                    /*TODO: Maintenance implementation*/
                    IconButton(modifier = Modifier.size(24.dp), onClick = { }) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.padding(5.dp))
                val maintenances = client.maintenances
                maintenances.forEach {
                    ClickableListItem(it.date.toString(), it.description) {

                    }
                    BasicDivider()
                }
            }
            BasicCard {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Projects",
                        style = MaterialTheme.typography.h2
                    )
                    IconButton(
                        onClick = { navigateToNewProject(client) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.padding(5.dp))
                client.projects.forEach {
                    ClickableListItem(it.name, if (it.isFinished()) "Finished" else "Active") {
                        navigateToProject(it)
                    }
                    BasicDivider()
                }
            }
        }
    }
}

@Composable
fun ClientEditingView(
    client: Client? = null,
    onClientSave: (Client) -> Unit,
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit = {}
) {
    val firstnameValue = remember { mutableStateOf(client?.fullname?.firstname ?: String()) }
    val lastnameValue = remember { mutableStateOf(client?.fullname?.lastname ?: String()) }
    val emailValue = remember { mutableStateOf(client?.emailAddress?.emailAddress ?: String()) }
    val postcodeValue =
        remember { mutableStateOf(client?.address?.postcode?.toString() ?: String()) }
    val cityValue = remember { mutableStateOf(client?.address?.city ?: String()) }
    val streetValue = remember { mutableStateOf(client?.address?.street ?: String()) }
    val housenumberValue =
        remember { mutableStateOf(client?.address?.houseNumber?.toString() ?: String()) }
    val phoneValue = remember { mutableStateOf(client?.phonenumber?.toString() ?: String()) }

    val isError = remember { mutableStateOf(false) }

    val context = LocalContext.current

    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = if (client != null) "Client Editing" else "Client Creating",
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

                    val newClient = Client(
                        id = client?.id ?: UUID.randomUUID(),
                        fullname = Fullname(firstnameValue.value, lastnameValue.value),
                        phonenumber = phoneValue.value.toLongOrNull(),
                        address = address,
                        emailAddress = email
                    )
                    onClientSave(newClient)
                    Toast.makeText(context, "$newClient saved", Toast.LENGTH_LONG).show()
                } else {
                    isError.value = true
                }
            }
        }
    }
}
/*@Preview
@Composable
fun PreviewClients() {
    SmorkTheme {
        Clients(bottomBar = { BottomNavigationBar() }, liveClients = exampleClients)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PeviewClientView() {
    SmorkTheme {
        ClientView(
            client = exampleClients[0],
            clientsProjects = exampleProjects.filter { it.clientId == exampleClients[0].id },
            bottomBar = { BottomNavigationBar() }
        )
    }
}*/

@Preview
@Composable
fun PreviewNewClient() {
    SmorkTheme {
        ClientEditingView(
            bottomBar = { BottomNavigationBar() },
            onClientSave = {}
        )
    }
}

@Preview
@Composable
fun PreviewClientEditView() {
    SmorkTheme {
        ClientEditingView(
            client = exampleClients[0],
            bottomBar = { BottomNavigationBar() },
            onClientSave = {}
        )
    }
}