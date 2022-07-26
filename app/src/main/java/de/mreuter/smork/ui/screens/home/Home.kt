package de.mreuter.smork.ui.screens.home

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.core.Date
import de.mreuter.smork.backend.core.Maintenance
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.exampleClients
import de.mreuter.smork.exampleProjects
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.theme.SmorkTheme
import java.time.LocalDate

@Composable
fun Home(
    projects: List<Project>,
    activeMaintenances: List<Maintenance>,
    navigateToProject: (Project) -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    BasicScaffold(bottomBar = { bottomBar() }, topBarTitle = stringResource(id = R.string.app_name)){
        BasicLazyColumn {
            BasicCard {
                Text(text = "Active Projects", style = MaterialTheme.typography.h2)
                Spacer(modifier = Modifier.padding(5.dp))
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    projects.forEach { project ->
                        ClickableListItem(
                            project.name,
                            project.client.toString()
                        ) {
                            navigateToProject(project)
                        }
                        if (project != projects.last())
                            BasicDivider()
                    }
                }
            }
            BasicCard {
                Text(
                    text = "Next Maintenances",
                    style = MaterialTheme.typography.h2
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    activeMaintenances.forEach {
                        BasicListItem(
                            topic = "${it.date}",
                            description = it.description
                        )
                        if (activeMaintenances.last() != it)
                            BasicDivider()
                    }
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_NO)
fun HomePreview() {
    SmorkTheme {
        Home(
            projects = exampleProjects,
            activeMaintenances = listOf(Maintenance(exampleClients[0], "Boiler", Date(LocalDate.now()))),
            bottomBar = { BottomNavigationBar() }
        )
    }
}