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
import de.mreuter.smork.backend.worker.domain.Worker
import de.mreuter.smork.ui.elements.*

@Composable
fun WorkerView(
    worker: Worker,
    navigateToEditView: (Worker) -> Unit = {},
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "${worker.fullname}",
        backNavigation = backNavigation,
        trailingAppBarIcons = {
            IconButton(onClick = { navigateToEditView(worker) }) {
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
                    description = worker.phonenumber?.toString() ?: ""
                )
                BasicDivider()
                Spacer(modifier = Modifier.padding(15.dp))
                BasicListItem(topic = "Email", description = worker.emailAddress?.toString() ?: "")
                BasicDivider()
                Spacer(modifier = Modifier.padding(15.dp))
                BasicListItem(topic = "Address", description = worker.address?.toString() ?: "")
                BasicDivider()
            }
        }
    }
}