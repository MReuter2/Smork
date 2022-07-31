package de.mreuter.smork.ui.project.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.backend.project.domain.Task
import de.mreuter.smork.exampleClients
import de.mreuter.smork.ui.utils.*
import de.mreuter.smork.ui.theme.SmorkTheme


@Composable
fun NewProject(
    preselectedClient: Client? = null,
    clients: List<Client>,
    onProjectSave: (Project) -> Unit = {},
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit = {}
) {
    val projectName = remember { mutableStateOf(String()) }
    val tasks = remember { mutableStateListOf<Task>() }

    val sortedClients = clients
        .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })

    val clientDropDown = DropDown(sortedClients)

    val showErrors = remember { mutableStateOf(false) }

    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "New Project",
        backNavigation = { backNavigation() }) {
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
                preselectedItem = preselectedClient,
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
            PrimaryButton(label = "Create") {
                if (clientDropDown.exposedMenuStateHolder.selectedItem != null &&
                    projectName.value.trim() != ""
                ) {
                    val client: Client =
                        clientDropDown.exposedMenuStateHolder.selectedItem as Client
                    val project =
                        Project(name = projectName.value, client = client)
                    project.addTasks(tasks)
                    onProjectSave(project)
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
fun PreviewNewProject() {
    SmorkTheme {
        NewProject(
            preselectedClient = exampleClients[0],
            clients = exampleClients,
            bottomBar = { BottomNavigationBar() },
        )
    }
}