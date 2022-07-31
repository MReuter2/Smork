package de.mreuter.smork.ui.client.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.exampleClients
import de.mreuter.smork.ui.utils.*
import de.mreuter.smork.ui.theme.SmorkTheme


@Composable
fun Clients(
    navigateToClient: (Client) -> Unit = {},
    navigateToNewClient: () -> Unit = {},
    clients: List<Client>,
    bottomBar: @Composable () -> Unit
) {

    val sortedClients = clients
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
    ) {
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


@Preview
@Composable
fun PreviewClients() {
    SmorkTheme {
        Clients(
            clients = exampleClients,
            bottomBar = { BottomNavigationBar() }
        )
    }
}