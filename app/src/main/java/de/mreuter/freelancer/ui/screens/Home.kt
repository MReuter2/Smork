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
import de.mreuter.freelancer.BottomBar
import de.mreuter.freelancer.PROJECTS
import de.mreuter.freelancer.TopBar
import de.mreuter.freelancer.stateHolder
import de.mreuter.freelancer.ui.elements.BasicListItem
import de.mreuter.freelancer.ui.elements.ClickableListItem
import de.mreuter.freelancer.ui.theme.FreelancerTheme
import de.mreuter.freelancer.ui.theme.Typography

@Composable
fun Home(navController: NavController?) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() }
    ) {
        LazyColumn(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .padding(horizontal = 40.dp)
        ) {
            item {
                Spacer(modifier = Modifier.padding(10.dp))
                Card(
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(text = "Active Projects", style = Typography.h2)
                        Spacer(modifier = Modifier.padding(10.dp))
                        stateHolder.getProjects().forEach {
                            if (!it.isFinished) ClickableListItem(
                                it.name,
                                "${it.client.fullname}"
                            ) {
                                navController?.navigate(PROJECTS)
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Next Maintenances",
                            style = Typography.h2,
                            modifier = Modifier.padding(top = 15.dp)
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        stateHolder.getMaintenances()
                            .forEach { if (!it.isFinished) BasicListItem(topic = "${it.date}", description = it.description) }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun HomePreview(){
    FreelancerTheme {
        Home(null)
    }
}