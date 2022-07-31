package de.mreuter.smork.ui.client.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.core.Person
import de.mreuter.smork.exampleClients
import de.mreuter.smork.ui.theme.SmorkTheme
import de.mreuter.smork.ui.utils.BottomNavigationBar
import de.mreuter.smork.ui.utils.person.PersonEditing


@Composable
fun ClientEditingView(
    client: Client,
    onClientSave: (Client) -> Unit = {},
    onClientDelete: (Client) -> Unit = {},
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    PersonEditing(
        person = client,
        onPersonSave = onClientSave as (Person) -> Unit,
        onPersonDelete = onClientDelete as (Person) -> Unit,
        backNavigation = backNavigation
    ) {
        bottomBar()
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
