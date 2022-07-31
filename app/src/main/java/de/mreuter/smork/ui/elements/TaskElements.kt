package de.mreuter.smork.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.project.domain.Task
import de.mreuter.smork.ui.theme.SmorkTheme


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
            text = task.description,
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
            text = task.description,
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
                        actionOnClick(Task(description = text.text))
                        text = TextFieldValue("")
                    }
                }
                .weight(1F),
            tint = MaterialTheme.colors.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskRow() {
    SmorkTheme {
        TaskRow(task = Task(description = "Task"), deleteAction = {})
    }
}
