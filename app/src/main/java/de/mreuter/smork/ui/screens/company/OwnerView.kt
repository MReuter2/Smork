package de.mreuter.smork.ui.screens.company

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
import de.mreuter.smork.backend.owner.domain.Owner
import de.mreuter.smork.ui.elements.*

@Composable
fun OwnerView(
    owner: Owner,
    navigateToEditView: (Owner) -> Unit = {},
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "${owner.fullname}",
        backNavigation = backNavigation,
        trailingAppBarIcons = {
            IconButton(onClick = { navigateToEditView(owner) }) {
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
                    description = owner.phonenumber?.toString() ?: ""
                )
                BasicDivider()
                Spacer(modifier = Modifier.padding(15.dp))
                BasicListItem(topic = "Email", description = owner.emailAddress?.toString() ?: "")
                BasicDivider()
                Spacer(modifier = Modifier.padding(15.dp))
                BasicListItem(topic = "Address", description = owner.address?.toString() ?: "")
                BasicDivider()
            }
        }
    }
}