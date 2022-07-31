package de.mreuter.smork.ui.screens.project

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.backend.project.domain.Task
import de.mreuter.smork.exampleClients
import de.mreuter.smork.exampleProjects
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.theme.Red
import de.mreuter.smork.ui.theme.SmorkTheme

@Composable
fun ProjectEditView(
    project: Project,
    clients: List<Client>,
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit = {},
    onProjectDelete: (Project) -> Unit = {},
    onProjectUpdate: (Project) -> Unit = {}
) {
    val projectName = remember { mutableStateOf(project.name) }
    val tasks = remember{ mutableStateListOf<Task>() }
    val tasksLoaded = remember{ mutableStateOf(false)}
    if(!tasksLoaded.value){
        project.tasks.forEach { tasks.add(it) }
        tasksLoaded.value = true
    }

    val sortedClients = clients
        .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
    val clientDropDown = DropDown(sortedClients)

    val showErrors = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }

    if (openDeleteDialog.value) {
        DeleteDialog(openDeleteDialog) { onProjectDelete(project) }
    }

    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "Project Editing",
        backNavigation = { backNavigation() },
        trailingAppBarIcons = {
            IconButton(
                onClick = { openDeleteDialog.value = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_delete_outline_24),
                    contentDescription = null,
                    tint = Red
                )
            }
        }
    ) {
        BasicLazyColumn {
            BasicOutlinedTextField(
                label = "Project name",
                value = projectName.value,
                onValueChange = { projectName.value = it },
                isError = showErrors.value && projectName.value.trim() == ""
            )
            Spacer(modifier = Modifier.padding(5.dp))
            clientDropDown.DropDownTextfield(
                label = "Client",
                preselectedItem = project.client,
                isError = showErrors.value && clientDropDown.exposedMenuStateHolder.selectedItem == null
            )
            Spacer(modifier = Modifier.padding(10.dp))
            BasicCard {
                Text(
                    text = "Tasks",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                tasks.forEach {
                    if (tasks.first() == it)
                        Spacer(modifier = Modifier.padding(5.dp))
                    TaskRow(task = it, deleteAction = { task -> tasks.remove(task) })
                    if (tasks.last() == it)
                        Spacer(modifier = Modifier.padding(5.dp))
                }
                NewTaskRow { task -> tasks.add(task) }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            PrimaryButton(label = "Save") {
                if (clientDropDown.exposedMenuStateHolder.selectedItem != null &&
                    projectName.value.trim() != ""
                ) {
                    val client: Client =
                        clientDropDown.exposedMenuStateHolder.selectedItem as Client
                    val updatedProject = Project(project.id, projectName.value, client, project.startDate, project.finishDate)
                    updatedProject.addTasks(tasks)
                    onProjectUpdate(updatedProject)
                } else {
                    showErrors.value = true
                }
            }
            Spacer(modifier = Modifier.padding(50.dp))
        }
    }
}

@Preview
@Composable
fun ProjectEditingPreview() {
    SmorkTheme {
        ProjectEditView(
            project = exampleProjects[0],
            clients = exampleClients,
            bottomBar = { BottomNavigationBar() },
        )
    }
}