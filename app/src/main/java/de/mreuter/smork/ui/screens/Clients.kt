package de.mreuter.smork.ui.screens

import android.widget.Toast
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


@Composable
fun Clients(navigateToClient: (Client) -> Unit = {}) {
    val sortedClients = stateHolder.getClients()
        .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
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
                style = Typography.subtitle1,
                modifier = Modifier.padding(top = 15.dp, bottom = 0.dp)
            )
            BasicCard {
                clientsDividedByFirstLetter[firstletters.indexOf(letter)].forEach { client ->
                    if (client.fullname.lastname[0].toString() == letter) {
                        ClickableListItem(
                            clickableText = client.fullname.toString(),
                            action = {
                                navigateToClient(client)
                            }
                        )
                    }
                    if (client != clientsDividedByFirstLetter[firstletters.indexOf(letter)].last()
                    ) {
                        Divider(
                            modifier = Modifier.padding(
                                horizontal = 2.dp,
                                vertical = 8.dp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClientView(
    client: Client,
    changeToEditView: (Client) -> Unit = {},
    navigateToNewProject: (Client) -> Unit = {}
) {
    BasicLazyColumn {
        Spacer(modifier = Modifier.padding(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${client.fullname.lastname}, ${client.fullname.firstname}",
                style = Typography.h1
            )
            IconButton(onClick = {
                changeToEditView(client)
            }) {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
            }
        }
        BasicCard {
            PersonInfoRow(
                label = "Phone number",
                value = client.phonenumber?.toString() ?: ""
            )
            Spacer(modifier = Modifier.padding(15.dp))
            PersonInfoRow(label = "Email", value = "")
            Spacer(modifier = Modifier.padding(15.dp))
            PersonInfoRow(label = "Address", value = client.address?.toString() ?: "")
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
                    style = Typography.h2
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))
            PersonInfoRow(label = "June 2022", value = "Boiler maintenance")/*TODO*/
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
                    style = Typography.h2
                )
                IconButton(onClick = {
                    navigateToNewProject(client)
                }) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.padding(5.dp))
            PersonInfoRow(label = "Bathroom restauration", value = "Finished")/*TODO*/
        }
    }
}

@Composable
fun ClientEditingView(client: Client? = null, navigateToClient: (Client) -> Unit = {}) {
    val firstnameValue = remember { mutableStateOf(client?.fullname?.firstname ?: String()) }
    val lastnameValue = remember { mutableStateOf(client?.fullname?.lastname ?: String()) }
    val postcodeValue =
        remember { mutableStateOf(client?.address?.postcode?.toString() ?: String()) }
    val cityValue = remember { mutableStateOf(client?.address?.city ?: String()) }
    val streetValue = remember { mutableStateOf(client?.address?.street ?: String()) }
    val housenumberValue =
        remember { mutableStateOf(client?.address?.houseNumber?.toString() ?: String()) }
    val phoneValue = remember { mutableStateOf(client?.phonenumber?.toString() ?: String()) }

    val context = LocalContext.current

    BasicLazyColumn {
        Spacer(modifier = Modifier.padding(20.dp))
        BasicOutlinedTextField(
            label = stringResource(id = R.string.firstname),
            value = firstnameValue.value,
            onValueChange = { firstnameValue.value = it }
        )
        Spacer(modifier = Modifier.padding(5.dp))
        BasicOutlinedTextField(
            label = stringResource(id = R.string.lastname),
            value = lastnameValue.value,
            onValueChange = { lastnameValue.value = it }
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
        Spacer(modifier = Modifier.padding(30.dp))
        PrimaryButton(label = "Save") {
            val newClient = Client(
                Fullname(firstnameValue.value, lastnameValue.value),
                phoneValue.value.toLongOrNull(),
                Address(
                    postcodeValue.value.toIntOrNull() ?: 0,
                    cityValue.value,
                    streetValue.value,
                    housenumberValue.value.toIntOrNull() ?: 0
                )
            )
            stateHolder.saveClient(newClient)
            Toast.makeText(context, "$newClient saved", Toast.LENGTH_LONG).show()
            navigateToClient(newClient)
        }
    }
}

@Composable
fun ClientCreatingView(navigateToClient: (Client) -> Unit = {}) {
    ClientEditingView {
        navigateToClient(it)
    }
}

@Composable
fun PersonInfoRow(label: String, value: String) {
    Text(
        text = label,
        style = Typography.subtitle1
    )
    Text(
        text = value,
        style = Typography.subtitle2,
        modifier = Modifier.padding(top = 10.dp, bottom = 6.dp, start = 3.dp)
    )
    Divider(modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
fun PreviewClients() {
    TestData()
    stateHolder.user = exampleOwner[0]
    FreelancerTheme {
        Scaffold(
            topBar = { TopBar()},
            bottomBar = { BottomNavigationBar(navController = rememberNavController())}
        ){
            Clients()
        }
    }
}

@Preview
@Composable
fun PeviewClientView() {
    FreelancerTheme {
        ClientView(client = Client(Fullname("Heinrich", "Albert")))
    }
}

@Preview
@Composable
fun PreviewNewClient() {
    FreelancerTheme {
        ClientCreatingView()
    }
}

@Preview
@Composable
fun PreviewClientEditView() {
    val navController = rememberNavController()
    val client = Client(Fullname("Barack", "Obama"))
    FreelancerTheme {
        ClientEditingView(client = client, navigateToClient = {
            navController.navigate(Screen.Clients.withArgs(it.uuid.toString()))
        })
    }
}