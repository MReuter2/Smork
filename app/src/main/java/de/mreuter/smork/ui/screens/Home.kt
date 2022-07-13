package de.mreuter.smork.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.Date
import de.mreuter.smork.backend.Maintenance
import de.mreuter.smork.backend.Project
import de.mreuter.smork.backend.exampleClients
import de.mreuter.smork.backend.exampleProjects
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.theme.FreelancerTheme
import de.mreuter.smork.ui.theme.Typography
import java.time.LocalDate
import java.util.*

@Composable
fun Home(
    activeProjects: List<Project>,
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
                    activeProjects.forEach { project ->
                        ClickableListItem(
                            project.name,
                            "${project.client.fullname}"
                        ) {
                            navigateToProject(project)
                        }
                        if (project != activeProjects.last())
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(uiMode = UI_MODE_NIGHT_NO)
fun HomePreview() {
    FreelancerTheme {
        Home(
            activeProjects = exampleProjects,
            activeMaintenances = listOf(Maintenance(exampleClients[0], "Ma", Date(LocalDate.now()))),
            bottomBar = { BottomNavigationBar() }
        )
    }
}