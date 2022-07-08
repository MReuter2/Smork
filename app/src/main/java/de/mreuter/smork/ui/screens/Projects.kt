package de.mreuter.smork.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.*
import de.mreuter.smork.backend.*
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.theme.*

@Composable
fun Projects(
    projects: List<Project>,
    navigateToNewProject: () -> Unit = {},
    navigateToProject: (Project) -> Unit = {}
) {
    val activeProjects = projects.filter { !it.isFinished }
    val finishedProjects = projects.filter { it.isFinished }

    BasicLazyColumn {
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
                        navigateToNewProject()
                    },
                    modifier = Modifier.then(Modifier.size(25.dp))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_add_24),
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
                        navigateToProject(project)
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
                        navigateToProject(project)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Project(project: Project) {
    val openStartDateDialog = remember { mutableStateOf(false) }
    val openFinishDateDialog = remember { mutableStateOf(false) }
    val startDate = remember { mutableStateOf(project.startDate) }
    val finishDate = remember { mutableStateOf(project.finishDate) }

    if (openStartDateDialog.value || openFinishDateDialog.value) {
        if (openStartDateDialog.value)
            DatePicker(
                { project.startDate = it },
                { openStartDateDialog.value = !openStartDateDialog.value })
        else
            DatePicker(
                { project.finishDate = it },
                { openFinishDateDialog.value = !openFinishDateDialog.value })
    } else {
        BasicLazyColumn {
            BasicCard {
                Text(
                    text = project.name,
                    style = Typography.h2
                )
                Text(
                    text = project.client.toString(),
                    style = Typography.subtitle2,
                    modifier = Modifier.padding(1.dp)
                )
            }

            /*TODO: Datum der Projekte (Datepicker und im backend überarbeiten)*/
            ExpandableCard(title = "Time period") {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(start = 6.dp, end = 14.dp, top = 0.dp, bottom = 6.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Start date")
                    ClickableText(
                        text = AnnotatedString(project.startDate.toString())
                    ) {
                        openStartDateDialog.value = !openStartDateDialog.value
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(start = 6.dp, end = 14.dp, top = 0.dp, bottom = 0.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "End date")
                    ClickableText(
                        text = AnnotatedString(project.finishDate.toString())
                    ) {
                        openFinishDateDialog.value = !openFinishDateDialog.value
                    }
                }
            }

            ExpandableCard(title = "Tasks") { TaskListWithCheckbox(tasks = project.tasks) }

            BasicCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Images", style = Typography.h2)
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.then(Modifier.size(25.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_chevron_right_24),
                            contentDescription = null
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(20.dp))

            PrimaryButton(label = "Save") {
                stateHolder.saveProject(project)
            }

            Spacer(modifier = Modifier.padding(10.dp))

            SecondaryButton(label = "Finish") {
                if (startDate.value != null && finishDate.value != null) {
                    project.finish(startDate.value!!, finishDate.value!!)
                }
            }
        }
    }
}

@Composable
fun TaskListWithCheckbox(tasks: List<Task>) {
    tasks.forEach {
        TaskRowWithCheckbox(task = it)
        if (it != tasks.last())
            BasicDivider()
    }
}

@Composable
fun TaskRowWithCheckbox(task: Task) {
    val taskCheck = remember { mutableStateOf(task.isFinished) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 6.dp, end = 14.dp, top = 0.dp, bottom = 0.dp)
            .fillMaxWidth()
            .clickable(onClick = { taskCheck.value = !taskCheck.value })
    ) {
        Text(
            text = task.taskDescription
        )
        Checkbox(
            checked = taskCheck.value,
            onCheckedChange = { task.isFinished = !task.isFinished; taskCheck.value = it },
            colors = CheckboxDefaults.colors(
                checkedColor = Purple
            )
        )
    }
}

@Composable
fun NewProject(
    preselectedClient: Client? = null,
    clients: List<Client>,
    navigateToProject: (Project) -> Unit = {}
) {
    val context = LocalContext.current
    val projectName = remember { mutableStateOf(String()) }
    val tasks = remember { mutableStateListOf<Task>() }

    val sortedClients = clients
        .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
    val clientDropDown = DropDown(sortedClients)

    BasicLazyColumn {
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
            NewTaskRow { task -> tasks.add(task) }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        PrimaryButton(label = "Create") {
            val client: Client = clientDropDown.exposedMenuStateHolder.selectedItem as Client
            val project = Project(projectName.value, client, tasks)
            client.addProject(project)
            stateHolder.saveProject(project)
            Toast.makeText(context, "Project created", Toast.LENGTH_LONG).show()
            navigateToProject(project)
        }
        Spacer(modifier = Modifier.padding(50.dp))
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
    TestData()
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewProject() {
    TestData()
    FreelancerTheme {
        Project(project = exampleProjects[0])
    }
}