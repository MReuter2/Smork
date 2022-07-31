package de.mreuter.smork.ui.client.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.ui.theme.SmorkTheme
import de.mreuter.smork.ui.utils.BottomNavigationBar
import de.mreuter.smork.ui.utils.person.PersonCreating

@Composable
fun ClientCreating(
    onClientSave: (Client) -> Unit = {},
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
){
    PersonCreating(
        onPersonSave = {fullname, address, emailAddress, phonenumber ->
            onClientSave(Client(fullname = fullname, address = address, emailAddress = emailAddress, phonenumber = phonenumber))
        },
        backNavigation = backNavigation
    ) {
        bottomBar()
    }
}

@Preview
@Composable
fun PreviewNewClient() {
    SmorkTheme {
        ClientCreating(
            bottomBar = { BottomNavigationBar() }
        )
    }
}