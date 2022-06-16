package de.mreuter.freelancer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import de.mreuter.freelancer.*
import de.mreuter.freelancer.R
import de.mreuter.freelancer.backend.*
import de.mreuter.freelancer.ui.elements.*
import de.mreuter.freelancer.ui.navigation.NEW_PROJECT
import de.mreuter.freelancer.ui.navigation.PROJECTS
import de.mreuter.freelancer.ui.rememberExposedMenuStateHolder
import de.mreuter.freelancer.ui.theme.*
import java.util.*

@Composable
fun Projects(navController: NavController? = null, projects: List<Project>) {
    val activeProjects = projects.filter { !it.isFinished }
    val finishedProjects = projects.filter { it.isFinished }

    BasicScaffoldWithLazyColumn(navController = navController) {
        Spacer(modifier = Modifier.padding(10.dp))
        BasicCard {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            ) {
                Text(text = "Active Projects", style = Typography.h2)
                IconButton(
                    onClick = {
                        navController?.navigate(NEW_PROJECT(null))
                    },
                    modifier = Modifier.then(Modifier.size(25.dp))
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Column(
                modifier = Modifier.padding(3.dp)
            ) {
                activeProjects.forEach { project ->
                    ClickableListItem(
                        project.name, "${project.client.fullname}"
                    ) {
                        navController?.navigate(
                            PROJECTS
                        )
                    }
                    if (activeProjects.last() != project)
                        Divider(modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp))
                }
            }
        }

        BasicCard {
            Text(
                text = "Finished Projects",
                style = Typography.h2
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Column(
                modifier = Modifier.padding(3.dp)
            ) {
                finishedProjects.forEach { project ->
                    ClickableListItem(
                        project.name, "${project.client.fullname}"
                    ) {
                        navController?.navigate(
                            PROJECTS
                        )
                    }
                    if (finishedProjects.last() != project)
                        Divider(
                            modifier = Modifier.padding(
                                horizontal = 2.dp,
                                vertical = 8.dp
                            )
                        )
                }
            }
        }
    }
}

@Composable
fun Project(project: Project) {
    BasicScaffoldWithLazyColumn(navController = null) {
        BasicCard {

        }

        BasicCard {

        }

        BasicCard {

        }
    }
}

@Composable
fun NewProject(
    navController: NavController? = null,
    clientID: UUID? = null,
    clients: List<Client>
) {
    val context = LocalContext.current
    val projectName = remember { mutableStateOf(String()) }
    val tasks = remember { mutableStateListOf<Task>() }

    val sortedClients = clients
        .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
    val exposedMenuStateHolder = rememberExposedMenuStateHolder(sortedClients)
    val clientDropDown = DropDown(sortedClients)

    BasicScaffoldWithLazyColumn(navController = navController) {
        Spacer(modifier = Modifier.padding(20.dp))
        BasicOutlinedTextField(
            label = "Project name",
            value = projectName.value,
            onValueChange = { projectName.value = it }
        )
        Spacer(modifier = Modifier.padding(5.dp))
        clientDropDown.DropDownTextfield(label = "Client")
        Spacer(modifier = Modifier.padding(10.dp))
        BasicCard {
                Text(
                    text = "Tasks",
                    style = Typography.subtitle1,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                tasks.forEach {
                    TaskRow(task = it)
                }
                NewTaskRow { task -> tasks.add(task); println(tasks.last().taskDescription) }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        PrimaryButton(label = "Create") {
            val client: Client = clientDropDown.exposedMenuStateHolder.selectedItem as Client
            client.addProject(Project(projectName.value, client, tasks))
            stateHolder.saveCompany(stateHolder.usersCompany() ?: throw RuntimeException())
            Toast.makeText(context, "Project created", Toast.LENGTH_LONG).show()
            navController?.navigate(PROJECTS)
        }
        Spacer(modifier = Modifier.padding(50.dp))
    }
}

@Composable
fun NewTaskRow(actionOnClick: (Task) -> Unit) {
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
                .width(250.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_outline_add_24),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (text.text != "") {
                        actionOnClick(Task(text.text))
                        text = TextFieldValue("")
                    }
                }
        )
    }
}

@Composable
fun TaskRow(task: Task) {
    Column {
        Text(
            text = task.taskDescription,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewProjects() {
    FreelancerTheme {
        Projects(projects = exampleProjects)
    }
}

@Preview
@Composable
fun PreviewNewProject() {
    FreelancerTheme {
        NewProject(clients = exampleClients)
    }
}