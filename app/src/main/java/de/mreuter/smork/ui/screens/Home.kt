package de.mreuter.smork.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.core.Maintenance
import de.mreuter.smork.backend.project.application.ProjectEntity
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.ui.elements.*

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
                            project.client.toString() //TODO
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

/*@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(uiMode = UI_MODE_NIGHT_NO)
fun HomePreview() {
    SmorkTheme {
        Home(
            projectEntities = exampleProjectEntities,
            activeMaintenances = listOf(Maintenance(exampleClientEntityEntities[0], "Ma", Date(LocalDate.now()))),
            bottomBar = { BottomNavigationBar() }
        )
    }
}*/