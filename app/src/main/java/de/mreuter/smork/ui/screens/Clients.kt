package de.mreuter.smork.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.compose.rememberNavController
import de.mreuter.smork.*
import de.mreuter.smork.R
import de.mreuter.smork.backend.*
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.navigation.Screen
import de.mreuter.smork.ui.theme.*
import java.util.*


@Composable
fun Clients(
    navigateToClient: (Client) -> Unit = {},
    navigateToNewClient: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    val sortedClients = stateHolder.getClients()
        .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
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
        ){
            BasicLazyColumn {
                var lastFirstLetter = '-'
                var currentList = mutableListOf<Client>()
                val clientsDividedByFirstLetter = mutableListOf<List<Client>>()
                val firstletters = mutableListOf<String>()
                sortedClients.forEach { client ->
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
                            if (client.fullname.lastname[0].toString() == letter) {
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClientView(
    client: Client,
    changeToEditView: (Client) -> Unit = {},
    navigateToNewProject: (Client) -> Unit = {},
    navigateToProject: (Project) -> Unit = {},
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit = {}
) {
    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "${ client.fullname }",
        backNavigation = backNavigation,
        trailingAppBarIcons = {
            IconButton(onClick = { changeToEditView(client) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_edit_24),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ){
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
                val maintenances = client.getMaintenances()
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
                client.getProjects().forEach {
                    ClickableListItem(it.name, if (it.isFinished) "Finished" else "Active") {
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
    navigateToClient: (Client) -> Unit = {},
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit = {}
) {
    val firstnameValue = remember { mutableStateOf(client?.fullname?.firstname ?: String()) }
    val lastnameValue = remember { mutableStateOf(client?.fullname?.lastname ?: String()) }
    val emailValue = remember { mutableStateOf(client?.email?.emailAddress ?: String()) }
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
        topBarTitle = if(client != null) "Client Editing" else "Client Creating",
        backNavigation = { backNavigation() }){
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
                if(firstnameValue.value.trim() != "" && lastnameValue.value.trim() != ""){
                    val address = if(
                        postcodeValue.value.toIntOrNull() != null
                        && postcodeValue.value.length == 5
                        && cityValue.value != ""
                        && streetValue.value != ""
                        && housenumberValue.value.toIntOrNull() != null
                    )
                        Address(
                        postcodeValue.value.toIntOrNull() ?: 0,
                            cityValue.value,
                            streetValue.value,
                            housenumberValue.value.toIntOrNull() ?: 0
                        )
                    else null
                    val email = if(emailValue.value.contains("@")) EmailAddress(emailValue.value)
                                else null
                    if(client != null){
                        client.fullname = Fullname(firstnameValue.value, lastnameValue.value)
                        client.phonenumber = phoneValue.value.toLongOrNull()
                        client.address = address
                        client.emailAddress = email
                        stateHolder.saveClient(client)
                        Toast.makeText(context, "$client saved", Toast.LENGTH_LONG).show()
                        navigateToClient(client)
                    }else {
                        val newClient = Client(
                            Fullname(firstnameValue.value, lastnameValue.value),
                            phoneValue.value.toLongOrNull(),
                            address,
                            email
                        )
                        stateHolder.saveClient(newClient)
                        Toast.makeText(context, "$newClient saved", Toast.LENGTH_LONG).show()
                        navigateToClient(newClient)
                    }
                }else{
                    isError.value = true
                }
            }
        }
    }
}

@Composable
fun ClientCreatingView(navigateToClient: (Client) -> Unit = {}, bottomBar: @Composable () -> Unit, backNavigation: () -> Unit = {}) {
    ClientEditingView (
        navigateToClient = { navigateToClient(it) },
        bottomBar = { bottomBar() },
        backNavigation = { backNavigation() }
    )
}

@Preview
@Composable
fun PreviewClients() {
    TestData()
    stateHolder.user = exampleOwner[0]
    FreelancerTheme {
        Clients(bottomBar = { BottomNavigationBar() })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PeviewClientView() {
    FreelancerTheme {
        ClientView(client = exampleClients[0], bottomBar = { BottomNavigationBar() })
    }
}

@Preview
@Composable
fun PreviewNewClient() {
    FreelancerTheme {
        ClientCreatingView(bottomBar = { BottomNavigationBar() })
    }
}

@Preview
@Composable
fun PreviewClientEditView() {
    val navController = rememberNavController()
    val client = Client(Fullname("Barack", "Obama"))
    FreelancerTheme {
            ClientEditingView(
                client = client,
                navigateToClient = { navController.navigate(Screen.Clients.withArgs(it.uuid.toString())) },
                bottomBar = { BottomNavigationBar(navController) }
            )
    }
}