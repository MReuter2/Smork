package de.mreuter.smork.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.client.application.ClientEntity
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.core.Date
import de.mreuter.smork.backend.core.Task
import de.mreuter.smork.backend.project.application.ProjectEntity
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.theme.*

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
                            project.name, project.client.toString() //TODO CLIENT
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
                            project.name, project.client.toString() //TODO CLIENT
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
fun Project(
    project: Project,
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit,
    navigateToEditView: (Project) -> Unit,
    onProjectUpdate: (Project) -> Unit
) {
    val openStartDateDialog = remember { mutableStateOf(false) }
    val openFinishDateDialog = remember { mutableStateOf(false) }

    if (openStartDateDialog.value || openFinishDateDialog.value) {
        if (openStartDateDialog.value)
            DatePicker(
                { project.startDate = Date(it) },
                { openStartDateDialog.value = !openStartDateDialog.value })
        else
            DatePicker(
                { project.finishDate = Date(it) },
                { openFinishDateDialog.value = !openFinishDateDialog.value })
    }
    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "Project",
        backNavigation = { backNavigation() },
        trailingAppBarIcons = {
            IconButton(
                onClick = { navigateToEditView(project) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_edit_24),
                    contentDescription = null
                )
            }
        }
    ) {
        BasicLazyColumn {
            BasicCard {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.h2
                )
                Text(
                    text = project.client.toString(), //TODO CLIENT
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.padding(1.dp)
                )
            }
            ExpandableCard(title = "Time period") {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(start = 6.dp, end = 14.dp, top = 0.dp, bottom = 6.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Start date", style = MaterialTheme.typography.subtitle1)
                    ClickableText(
                        text = AnnotatedString(project.startDate?.toString() ?: "Select date"),
                        style = MaterialTheme.typography.subtitle1
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
                    Text(text = "End date", style = MaterialTheme.typography.subtitle1)
                    ClickableText(
                        text = AnnotatedString(project.finishDate?.toString() ?: "Select date"),
                        style = MaterialTheme.typography.subtitle1
                    ) {
                        openFinishDateDialog.value = !openFinishDateDialog.value
                    }
                }
            }

            ExpandableCard(title = "Tasks") { TaskListWithCheckbox(tasks = listOf()) } //TODO TASKS

            ExpandableCard(title = "Images") {
                val context = LocalContext.current
                val imageData: MutableState<Uri?> = remember { mutableStateOf(null) }
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { imageData.value = it }
                )
                /*TODO: ImageView*/
                imageData.let {
                    val bitmap: MutableState<Bitmap?> = remember { mutableStateOf(null) }
                    val uri = it.value
                    if (uri != null) {
                        if (Build.VERSION.SDK_INT < 28) {
                            bitmap.value =
                                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, uri)
                            bitmap.value = ImageDecoder.decodeBitmap(source)
                        }

                        bitmap.value?.let { btm ->
                            Image(
                                bitmap = btm.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_add_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            launcher.launch("image/*")
                        }
                )
            }

            Spacer(modifier = Modifier.padding(20.dp))

            PrimaryButton(label = "Save") {
                onProjectUpdate(project)
            }

            Spacer(modifier = Modifier.padding(10.dp))

            /*SecondaryButton(label = "Finish") {
                if (startDate.value != null && finishDate.value != null) {
                }
            }*/
        }
    }
}

@Composable
fun ProjectEditView(
    project: Project,
    clients: List<Client>,
    bottomBar: @Composable () -> Unit,
    navigateToProjects: () -> Unit,
    onProjectDelete: (Project) -> Unit,
    onProjectUpdate: (Project) -> Unit
) {
    val projectName = remember { mutableStateOf(project.name) }
    val tasks = remember { mutableStateListOf<Task>() } //TODO TASKS
    val sortedClients = clients
        .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
    val clientDropDown = DropDown(sortedClients)

    val showErrors = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }

    if (openDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDeleteDialog.value = false
            },
            title = {
                Text(text = "Are you sure?")
            },
            text = {},
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ClickableText(
                        text = AnnotatedString("Cancel"),
                        onClick = { openDeleteDialog.value = false }
                    )
                    PrimaryButton(label = "Yes") {
                        /*TODO: Deleting throw Errors*/
                        onProjectDelete(project)
                    }
                }
            }
        )
    }

    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "Project Editing",
        backNavigation = { navigateToProjects() },
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
                preselectedItem = project.client, //TODO CLIENT
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
                    project.name = projectName.value
                    val newProject = Project(project.id, projectName.value, client, project.startDate, project.finishDate)
                    newProject.addTasks(tasks)
                    onProjectUpdate(project)
                } else {
                    showErrors.value = true
                }
            }
            Spacer(modifier = Modifier.padding(50.dp))
        }
    }
}

@Composable
fun NewProject(
    preselectedClient: Client? = null,
    clients: List<Client>,
    onProjectSave: (Project) -> Unit,
    bottomBar: @Composable () -> Unit,
    backNavigation: () -> Unit
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

@Composable
fun TaskListWithCheckbox(tasks: List<Task>) {
    tasks.forEach {
        TaskRowWithCheckbox(task = it)
        if (it != tasks.last()) {
            BasicDivider()
        } else {
            Spacer(modifier = Modifier.padding(5.dp))
        }
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
            text = task.taskDescription,
            style = TextStyle(textDecoration = if (taskCheck.value) TextDecoration.LineThrough else TextDecoration.None)
        )
        Checkbox(
            checked = taskCheck.value,
            onCheckedChange = { task.isFinished = !task.isFinished; taskCheck.value = it },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.primary
            )
        )
    }
}

@Composable
fun TaskRow(task: Task, deleteAction: (Task) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = task.taskDescription,
            modifier = Modifier
                .padding(8.dp)
                .weight(4F),
            style = MaterialTheme.typography.subtitle1
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_remove_24),
            contentDescription = null,
            modifier = Modifier
                .clickable { deleteAction(task) }
                .weight(2F),
            tint = MaterialTheme.colors.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskRow() {
    SmorkTheme {
        TaskRow(task = Task("Task"), deleteAction = {})
    }
}

/*@Preview
@Composable
fun PreviewProjects() {
    SmorkTheme {
        Projects(
            projectEntities = exampleProjectEntities,
            bottomBar = { BottomNavigationBar() }
        )
    }
}

@Preview
@Composable
fun PreviewNewProject() {
    SmorkTheme {
        NewProject(
            preselectedClientEntityEntity = exampleClientEntityEntities[0],
            clientEntityEntities = exampleClientEntityEntities,
            bottomBar = { BottomNavigationBar() },
            backNavigation = {},
            onProjectSave = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewProject() {
    SmorkTheme {
        Project(
            projectEntity = exampleProjectEntities[0],
            bottomBar = { BottomNavigationBar() },
            backNavigation = {},
            navigateToEditView = {},
            onProjectUpdate = {}
        )
    }
}*/