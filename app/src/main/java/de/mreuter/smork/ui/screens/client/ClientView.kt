package de.mreuter.smork.ui.screens.client

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.exampleClients
import de.mreuter.smork.exampleProjects
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.theme.SmorkTheme


@Composable
fun ClientView(
    client: Client,
    projects: List<Project>,
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
            /*BasicCard {
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
            }*/
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
                projects.forEach {
                    ClickableListItem(it.name, if (it.isFinished()) "Finished" else "Active") {
                        navigateToProject(it)
                    }
                    BasicDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PeviewClientView() {
    SmorkTheme {
        ClientView(
            client = exampleClients[0],
            projects = exampleProjects,
            bottomBar = { BottomNavigationBar() }
        )
    }
}