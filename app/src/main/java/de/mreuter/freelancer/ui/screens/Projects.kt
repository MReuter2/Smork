package de.mreuter.freelancer.ui.screens

import android.os.Build
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
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
import de.mreuter.freelancer.ui.navigation.PROJECT
import de.mreuter.freelancer.ui.navigation.PROJECTS
import de.mreuter.freelancer.ui.rememberExposedMenuStateHolder
import de.mreuter.freelancer.ui.theme.*
import java.time.LocalDate
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
                        navController?.navigate(
                            PROJECT(project.uuid)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Project(project: Project) {
    val timePeriodExpanded = remember { mutableStateOf(false) }
    val updatedProject = project
    BasicScaffoldWithLazyColumn(navController = null) {
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
            /*TODO: Expandable Card*/
            ExpandableCard(title = "Time period"){
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(start = 6.dp, end = 14.dp, top = 0.dp, bottom = 6.dp)
                        .fillMaxWidth()
                ) {
                    var startDate = LocalDate.now()
                    Text(text = "Start date")
                    ClickableText(
                        text = AnnotatedString(project.startDate.toString()),
                        onClick = {

                        })
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(start = 6.dp, end = 14.dp, top = 0.dp, bottom = 0.dp)
                        .fillMaxWidth()
                ) {
                    val endDate = remember { mutableStateOf(Long)}
                    Text(text = "End date")
                    Text(text = "22.01.2022")
                }
            }

        ExpandableCard(title = "Tasks") { TaskListWithCheckbox(tasks = project.tasks) }

        BasicCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "Images", style = Typography.h2)
                IconButton(onClick = { /*TODO*/ },
                    modifier = Modifier.then(Modifier.size(25.dp))) {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_chevron_right_24), contentDescription = null)
                }
            }
        }

        Spacer(modifier = Modifier.padding(20.dp))

        PrimaryButton(label = "Save") {
            stateHolder.saveProject(project)
        }

        Spacer(modifier = Modifier.padding(10.dp))

        SecondaryButton(label = "Finish") {
            project.finish(Date(), Date())
        }

        Spacer(modifier = Modifier.padding(20.dp))
    }
}

@Composable
fun TaskListWithCheckbox(tasks: List<Task>){
    tasks.forEach{
        TaskRowWithCheckbox(task = it)
        if(it != tasks.last())
            BasicDivider()
    }
}

@Composable
fun TaskRowWithCheckbox(task: Task){
    val taskCheck = remember{ mutableStateOf(task.isFinished) }
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
            onCheckedChange = {task.isFinished = !task.isFinished; taskCheck.value = it},
            colors = CheckboxDefaults.colors(
                checkedColor = Purple
            )
        )
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
                NewTaskRow { task -> tasks.add(task) }
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
fun PreviewProject(){
    TestData()
    FreelancerTheme {
        Project(project = exampleProjects[0])
    }
}