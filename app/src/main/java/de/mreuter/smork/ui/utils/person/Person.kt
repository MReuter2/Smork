package de.mreuter.smork.ui.utils.person

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.core.Person
import de.mreuter.smork.ui.utils.*

@Composable
fun PersonInfo(
    person: Person,
    navigateToEditView: (Person) -> Unit = {},
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit = {},
    additionalContent: @Composable () -> Unit = {}
) {
    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "${person.fullname}",
        backNavigation = backNavigation,
        trailingAppBarIcons = {
            IconButton(onClick = { navigateToEditView(person) }) {
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
                    description = person.phonenumber?.toString() ?: ""
                )
                BasicDivider()
                Spacer(modifier = Modifier.padding(15.dp))
                BasicListItem(topic = "Email", description = person.emailAddress?.toString() ?: "")
                BasicDivider()
                Spacer(modifier = Modifier.padding(15.dp))
                BasicListItem(topic = "Address", description = person.address?.toString() ?: "")
                BasicDivider()
            }
            additionalContent()
        }
    }
}