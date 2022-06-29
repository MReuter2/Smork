package de.mreuter.smork.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.mreuter.smork.backend.Maintenance
import de.mreuter.smork.backend.Project
import de.mreuter.smork.backend.exampleClients
import de.mreuter.smork.backend.exampleProjects
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.navigation.PROJECTS
import de.mreuter.smork.ui.theme.FreelancerTheme
import de.mreuter.smork.ui.theme.Typography
import java.util.*

@Composable
fun Home(
    navController: NavController?,
    activeProjects: List<Project>,
    activeMaintenances: List<Maintenance>
) {
    BasicScaffoldWithLazyColumn(navController = navController) {
        Spacer(modifier = Modifier.padding(10.dp))
        BasicCard {
                Text(text = "Active Projects", style = Typography.h2)
                Spacer(modifier = Modifier.padding(5.dp))
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    activeProjects.forEach {
                        ClickableListItem(
                            it.name,
                            "${it.client.fullname}"
                        ) {
                            navController?.navigate(PROJECTS)
                        }
                        if (it != activeProjects.last())
                            BasicDivider()
                    }
                }
        }
        BasicCard {
                Text(
                    text = "Next Maintenances",
                    style = Typography.h2
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

@Composable
@Preview
fun HomePreview() {
    FreelancerTheme {
        Home(null, exampleProjects, listOf(Maintenance(exampleClients[0], "Ma", Date(2022, 6, 20))))
    }
}