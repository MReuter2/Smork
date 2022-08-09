package de.mreuter.smork.ui.client.views

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.core.Person
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.exampleClients
import de.mreuter.smork.exampleProjects
import de.mreuter.smork.ui.theme.SmorkTheme
import de.mreuter.smork.ui.utils.BasicCard
import de.mreuter.smork.ui.utils.BasicDivider
import de.mreuter.smork.ui.utils.BottomNavigationBar
import de.mreuter.smork.ui.utils.ClickableListItem
import de.mreuter.smork.ui.utils.person.PersonInfo


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
    PersonInfo(
        person = client,
        navigateToEditView = navigateToEditView as (Person) -> Unit,
        backNavigation = backNavigation,
        bottomBar = { bottomBar() }
    ) {
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
                if(projects.last() != it){
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