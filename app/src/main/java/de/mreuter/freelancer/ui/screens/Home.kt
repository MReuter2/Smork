package de.mreuter.freelancer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.mreuter.freelancer.TopBar
import de.mreuter.freelancer.backend.Maintenance
import de.mreuter.freelancer.backend.Project
import de.mreuter.freelancer.backend.exampleClients
import de.mreuter.freelancer.backend.exampleProjects
import de.mreuter.freelancer.stateHolder
import de.mreuter.freelancer.ui.elements.*
import de.mreuter.freelancer.ui.navigation.BottomNavigationBar
import de.mreuter.freelancer.ui.navigation.PROJECTS
import de.mreuter.freelancer.ui.theme.FreelancerTheme
import de.mreuter.freelancer.ui.theme.Typography
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
                            topic = "${it.date.day}.${it.date.month}.${it.date.year}",
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