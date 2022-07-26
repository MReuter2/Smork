package de.mreuter.smork.ui.screens.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.core.Task
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.exampleProjects
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.theme.SmorkTheme

@Composable
fun Projects(
    projects: List<Project>,
    navigateToNewProject: () -> Unit = {},
    navigateToProject: (Project) -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    val activeProjectEntities = mutableListOf<Project>()
    val finishedProjectEntities = mutableListOf<Project>()
    projects.forEach { project ->
        if (!project.isFinished()) {
            activeProjectEntities.add(project)
        } else {
            finishedProjectEntities.add(project)
        }
    }

    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "Projects",
        trailingAppBarIcons = {
            IconButton(onClick = { navigateToNewProject() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_add_24),
                    contentDescription = null
                )
            }
        }
    ) {
        BasicLazyColumn {
            BasicCard {
                Text(text = "Active Projects", style = MaterialTheme.typography.h2)
                Spacer(modifier = Modifier.padding(10.dp))
                Column(
                    modifier = Modifier.padding(3.dp)
                ) {
                    activeProjectEntities.forEach { project ->
                        ClickableListItem(
                            project.name, project.client.toString()
                        ) {
                            navigateToProject(project)
                        }
                        if (activeProjectEntities.last() != project)
                            Divider(modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp))
                    }
                }
            }

            BasicCard {
                Text(
                    text = "Finished Projects",
                    style = MaterialTheme.typography.h2
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Column(
                    modifier = Modifier.padding(3.dp)
                ) {
                    finishedProjectEntities.forEach { project ->
                        ClickableListItem(
                            project.name, project.client.toString()
                        ) {
                            navigateToProject(project)
                        }
                        if (finishedProjectEntities.last() != project)
                            Divider(modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun NewTaskRow(actionOnClick: (Task) -> Unit = {}) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            placeholder = { Text(text = "New task") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            ),
            modifier = Modifier
                .height(50.dp)
                .weight(6F)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_outline_add_24),
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    if (text.text != "") {
                        actionOnClick(Task(text.text))
                        text = TextFieldValue("")
                    }
                }
                .weight(1F),
            tint = MaterialTheme.colors.onSurface
        )
    }
}


@Preview
@Composable
fun PreviewProjects() {
    SmorkTheme {
        Projects(
            projects = exampleProjects,
            bottomBar = { BottomNavigationBar() }
        )
    }
}