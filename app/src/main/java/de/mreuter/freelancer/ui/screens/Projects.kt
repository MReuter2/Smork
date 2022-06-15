package de.mreuter.freelancer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import de.mreuter.freelancer.*
import de.mreuter.freelancer.backend.*
import de.mreuter.freelancer.ui.elements.ClickableListItem
import de.mreuter.freelancer.ui.navigation.NEW_PROJECT
import de.mreuter.freelancer.ui.navigation.PROJECTS
import de.mreuter.freelancer.ui.rememberExposedMenuStateHolder
import de.mreuter.freelancer.ui.theme.*
import java.util.*

@Composable
fun Projects(navController: NavController? = null) {
    val activeProjects = stateHolder.getProjects().filter { !it.isFinished }
    val finishedProjects = stateHolder.getProjects().filter { it.isFinished }

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
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp)
                        ) {
                            Text(text = "Active Projects", style = Typography.h2)
                            IconButton(onClick = {
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
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    }
                }

                Card(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Finished Projects",
                            style = Typography.h2,
                            modifier = Modifier.padding(vertical = 15.dp)
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
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    }
                }
            }
        }
    }
}



@Composable
fun NewProject(navController: NavController? = null, clientID: UUID? = null) {
    val context = LocalContext.current
    val projectName = remember { mutableStateOf(String()) }
    val tasks = remember { mutableStateListOf<Task>() }

    val sortedClients = stateHolder.getClients()
        .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
    val stateHolder = rememberExposedMenuStateHolder(sortedClients)

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() }
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            item {
                Spacer(modifier = Modifier.padding(20.dp))
                OutlinedTextField(
                    value = projectName.value,
                    onValueChange = { projectName.value = it },
                    label = { Text(text = "Project name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = defaultTextFieldColors()
                )
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = stateHolder.value,
                    onValueChange = {},
                    label = { Text(text = "Client") },
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = stateHolder.icon,
                            contentDescription = null,
                            Modifier.clickable {
                                stateHolder.onEnabled(!(stateHolder.enabled))
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { stateHolder.onSize(it.size.toSize()) }
                )
                DropdownMenu(
                    expanded = stateHolder.enabled,
                    onDismissRequest = { stateHolder.onEnabled(false) },
                    modifier = Modifier.width(with(LocalDensity.current) { stateHolder.size.width.toDp() })
                ) {
                    stateHolder.items.forEachIndexed { index, s ->
                        DropdownMenuItem(
                            onClick = {
                                stateHolder.onSelectedIndex(index)
                                stateHolder.onEnabled(false)
                            }
                        ) {
                            Text(text = s)
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "Tasks",
                            style = Typography.subtitle1,
                            modifier = Modifier.padding(5.dp)
                        )
                        tasks.forEach {
                            TaskRow(task = it)
                        }
                        NewTaskRow { task -> tasks.add(task); println(tasks.last().taskDescription) }
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
                OutlinedButton(
                    shape = CircleShape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = ButtonBackgroundColor,
                        contentColor = ButtonContentColor
                    ),
                    onClick = {
                        navController?.navigate(PROJECTS)
                        /*TODO: Save Project*/
                        Toast.makeText(context, "Project created", Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Create",
                        style = TextStyle(color = ButtonContentColor)
                    )
                }
                Spacer(modifier = Modifier.padding(50.dp))
            }
        }
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
            imageVector = Icons.Outlined.Add,
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
        Projects()
    }
}

@Preview
@Composable
fun PreviewNewProject() {
    FreelancerTheme {
        NewProject()
    }
}