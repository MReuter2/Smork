package de.mreuter.freelancer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import de.mreuter.freelancer.*
import de.mreuter.freelancer.backend.Address
import de.mreuter.freelancer.backend.Client
import de.mreuter.freelancer.backend.Fullname
import de.mreuter.freelancer.backend.exampleClients
import de.mreuter.freelancer.ui.elements.ClickableListItem
import de.mreuter.freelancer.ui.navigation.CLIENTS
import de.mreuter.freelancer.ui.navigation.CLIENT_PROFILE
import de.mreuter.freelancer.ui.theme.*


@Composable
fun Clients(navController: NavController? = null) {
    val sortedClients = stateHolder.getClients()
        .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() },
        content = {
            LazyColumn(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(horizontal = 40.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.padding(10.dp))

                    var lastFirstLetter = '-'
                    var currentList = mutableListOf<Client>()
                    val clientsDividedByFirstLetter = mutableListOf<List<Client>>()
                    val firstletters = mutableListOf<String>()
                    sortedClients.forEach{  client ->
                        val currentFirstLetter = client.fullname.lastname[0]
                        if(currentFirstLetter == lastFirstLetter){
                            currentList.add(client)
                        }else
                            if(currentFirstLetter != lastFirstLetter){
                                firstletters.add(currentFirstLetter.uppercase())
                                if(currentList.isNotEmpty())
                                    clientsDividedByFirstLetter.add(currentList)
                                lastFirstLetter = currentFirstLetter
                                currentList = mutableListOf(client)
                            }
                        if(sortedClients.last() == client && currentList.isNotEmpty())
                            clientsDividedByFirstLetter.add(currentList)
                    }
                    firstletters.forEach { letter ->
                        Text(
                            text = letter,
                            style = Typography.subtitle1,
                            modifier = Modifier.padding(top = 15.dp, bottom = 5.dp)
                        )
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(
                                        start = 10.dp,
                                        end = 10.dp,
                                        top = 5.dp,
                                        bottom = 10.dp
                                    )
                                ) {
                                    clientsDividedByFirstLetter[firstletters.indexOf(letter)].forEach{ client ->
                                        if(client.fullname.lastname[0].toString() == letter) {
                                            ClickableListItem(
                                                clickableText = client.fullname.toString(),
                                                action = {
                                                    navController?.navigate(
                                                        CLIENT_PROFILE(
                                                            client.uuid,
                                                            false
                                                        )
                                                    )
                                                }
                                            )
                                        }
                                        if(client != clientsDividedByFirstLetter[firstletters.indexOf(letter)].last()){
                                            Divider(modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp))
                                        }
                                    }
                                }
                            }
                    }
                    Spacer(modifier = Modifier.padding(50.dp))
                }
            }
        }
    )
}

@Composable
fun ClientProfile(client: Client, edit: Boolean) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() },
        content = {
            if (!edit) {
                ClientView(client)
            } else {
                ClientEditView(client)
            }
        }
    )
}

@Composable
fun ClientEditView(client: Client, navController: NavController? = null) {
    val firstnameValue = remember{ mutableStateOf(String())}
    firstnameValue.value = client.fullname.firstname
    val lastnameValue = remember{ mutableStateOf(String())}
    lastnameValue.value = client.fullname.lastname
    val postcodeValue = remember{ mutableStateOf(String())}
    postcodeValue.value = client.address?.postcode?.toString() ?: ""
    val cityValue = remember{ mutableStateOf(String())}
    cityValue.value = client.address?.city ?: ""
    val streetValue = remember{ mutableStateOf(String())}
    streetValue.value = client.address?.street ?: ""
    val housenumberValue = remember{ mutableStateOf(String())}
    housenumberValue.value = client.address?.houseNumber?.toString() ?: ""
    val phoneValue = remember{ mutableStateOf(String())}
    phoneValue.value = client.phonenumber?.toString() ?: ""

    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() },
        content = {
            Column(
                modifier = Modifier.padding(horizontal = 40.dp)
            ) {
                    Spacer(modifier = Modifier.padding(20.dp))
                    OutlinedTextField(
                        value = firstnameValue.value,
                        onValueChange = {
                            firstnameValue.value = it
                        },
                        label = { Text(text = "First name") },
                        maxLines = 1,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        colors = defaultTextFieldColors()
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = lastnameValue.value,
                        onValueChange = {
                            lastnameValue.value = it
                        },
                        label = { Text(text = "Last name") },
                        maxLines = 1,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        colors = defaultTextFieldColors()
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row{
                        OutlinedTextField(
                            value = postcodeValue.value,
                            onValueChange = {
                                if(it.isDigitsOnly() && (postcodeValue.value.length < 5 || it.length <= 5))
                                    postcodeValue.value = it
                            },
                            label = { Text(text = "Postcode") },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(90.dp),
                            colors = defaultTextFieldColors()
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        OutlinedTextField(
                            value = cityValue.value,
                            onValueChange = {
                                cityValue.value = it
                            },
                            label = { Text(text = "City") },
                            maxLines = 1,
                            modifier = Modifier,
                            colors = defaultTextFieldColors()
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row {
                        OutlinedTextField(
                            value = streetValue.value,
                            onValueChange = {
                                streetValue.value = it
                            },
                            label = { Text(text = "Street") },
                            maxLines = 1,
                            modifier = Modifier
                                .width(230.dp),
                            colors = defaultTextFieldColors()
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        OutlinedTextField(
                            value = housenumberValue.value,
                            onValueChange = {
                                if(it.isDigitsOnly())
                                    housenumberValue.value = it
                            },
                            label = { Text(text = "Nr") },
                            maxLines = 1,
                            modifier = Modifier,
                            colors = defaultTextFieldColors()
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = phoneValue.value,
                        onValueChange = {
                            if(it.isDigitsOnly())
                                phoneValue.value = it
                        },
                        label = { Text(text = "Phone number") },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        colors = defaultTextFieldColors()
                    )
                    Spacer(modifier = Modifier.padding(30.dp))
                    OutlinedButton(
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = ButtonBackgroundColor,
                            contentColor = ButtonContentColor
                        ),
                        onClick = {
                            val newClient = Client(
                                Fullname(firstnameValue.value, lastnameValue.value),
                                phoneValue.value.toLongOrNull(),
                                Address(postcodeValue.value.toIntOrNull() ?: 0, cityValue.value, streetValue.value, 10)
                            )
                            /*TODO:client.update(newClient)*/
                            Toast.makeText(context, "$client saved", Toast.LENGTH_LONG).show()
                            navController?.navigate(CLIENTS)
                                  },
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    ){
                        Text(
                            text = "Save",
                            style = TextStyle(color = ButtonContentColor)
                        )
                    }
            }
        }
    )
}

@Composable
fun ClientView(client: Client, navController: NavController? = null) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally),
        content = {
            item {
                Spacer(modifier = Modifier.padding(20.dp))
                Row(modifier = Modifier) {
                    Text(
                        text = "${client.fullname.lastname}, ${client.fullname.firstname}",
                        style = Typography.h1,
                        modifier = Modifier
                    )
                    IconButton(onClick = {
                        navController?.navigate("clientprofile/$client/true")
                    }) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.padding(15.dp))
                Card {
                    Column(
                        modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp)
                    ) {
                        PersonInfoRow(
                            label = "Phone number",
                            value = client.phonenumber?.toString() ?: ""
                        )
                        Spacer(modifier = Modifier.padding(15.dp))
                        PersonInfoRow(label = "Email", value = "")
                        Spacer(modifier = Modifier.padding(15.dp))
                        PersonInfoRow(label = "Address", value = client.address?.toString() ?: "")
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Card {
                    Column(
                        modifier = Modifier.padding(bottom = 15.dp, start = 20.dp, end = 20.dp)
                    ) {
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
                        PersonInfoRow(label = "June 2022", value = "Boiler maintenance")
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Card {
                    Column(
                        modifier = Modifier.padding(bottom = 15.dp, start = 20.dp, end = 20.dp)
                    ) {
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
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                            }
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                        PersonInfoRow(label = "Bathroom restauration", value = "Finished")
                    }
                }
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    )
}

@Composable
fun NewClient(navController: NavController? = null){
    val firstnameValue = remember{ mutableStateOf(String())}
    val lastnameValue = remember{ mutableStateOf(String())}
    val postcodeValue = remember{ mutableStateOf(String())}
    val cityValue = remember{ mutableStateOf(String())}
    val streetValue = remember{ mutableStateOf(String())}
    val housenumberValue = remember{ mutableStateOf(String())}
    val phoneValue = remember{ mutableStateOf(String())}

    val context = LocalContext.current

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() },
        content = {
            Column(
                modifier = Modifier.padding(horizontal = 40.dp)
            ) {
                    Spacer(modifier = Modifier.padding(20.dp))
                    OutlinedTextField(
                        value = firstnameValue.value,
                        onValueChange = {
                            firstnameValue.value = it
                        },
                        label = { Text(text = "First name") },
                        maxLines = 1,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        colors = defaultTextFieldColors()
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = lastnameValue.value,
                        onValueChange = {
                            lastnameValue.value = it
                        },
                        label = { Text(text = "Last name") },
                        maxLines = 1,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        colors = defaultTextFieldColors()
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row{
                        OutlinedTextField(
                            value = postcodeValue.value,
                            onValueChange = {
                                if(it.isDigitsOnly() && (postcodeValue.value.length < 5 || it.length <= 5))
                                    postcodeValue.value = it
                            },
                            label = { Text(text = "Postcode") },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(90.dp),
                            colors = defaultTextFieldColors()
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        OutlinedTextField(
                            value = cityValue.value,
                            onValueChange = {
                                cityValue.value = it
                            },
                            label = { Text(text = "City") },
                            maxLines = 1,
                            modifier = Modifier,
                            colors = defaultTextFieldColors()
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Row {
                        OutlinedTextField(
                            value = streetValue.value,
                            onValueChange = {
                                streetValue.value = it
                            },
                            label = { Text(text = "Street") },
                            maxLines = 1,
                            modifier = Modifier
                                .width(230.dp),
                            colors = defaultTextFieldColors()
                        )
                        Spacer(modifier = Modifier.padding(5.dp))
                        OutlinedTextField(
                            value = housenumberValue.value,
                            onValueChange = {
                                if(it.isDigitsOnly())
                                    housenumberValue.value = it
                            },
                            label = { Text(text = "Nr") },
                            maxLines = 1,
                            modifier = Modifier,
                            colors = defaultTextFieldColors()
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    OutlinedTextField(
                        value = phoneValue.value,
                        onValueChange = {
                            if(it.isDigitsOnly())
                                phoneValue.value = it
                        },
                        label = { Text(text = "Phone number") },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        colors = defaultTextFieldColors()
                    )
                    Spacer(modifier = Modifier.padding(30.dp))
                    OutlinedButton(
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = ButtonBackgroundColor,
                            contentColor = ButtonContentColor
                        ),
                        onClick = {
                            val newClient = Client(
                                Fullname(firstnameValue.value, lastnameValue.value),
                                phoneValue.value.toLongOrNull(),
                                Address(postcodeValue.value.toIntOrNull() ?: 0, cityValue.value, streetValue.value, 10)
                            )
                            /*TODO:newClient.save()*/
                            Toast.makeText(context, "$newClient saved", Toast.LENGTH_LONG).show()
                            navController?.navigate(CLIENTS)
                        },
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                            .fillMaxWidth()
                    ){
                        Text(
                            text = "Save",
                            style = TextStyle(color = ButtonContentColor)
                        )
                    }
                }
        }
    )
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
    FreelancerTheme {
        Clients()
    }
}

@Preview
@Composable
fun PeviewClientProfile() {
    FreelancerTheme {
        ClientProfile(client = Client(Fullname("Heinrich", "Albert")), false)
    }
}

@Preview
@Composable
fun PreviewNewClient(){
    FreelancerTheme {
        NewClient()
    }
}

@Preview
@Composable
fun PreviewClientEditView(){
    FreelancerTheme {
        ClientEditView(client = Client(Fullname("Barack", "Obama")))
    }
}