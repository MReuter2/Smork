package de.mreuter.smork.ui.elements

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.ui.theme.SmorkTheme
import de.mreuter.smork.ui.theme.defaultTextFieldColors


@Composable
fun BasicOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(),
        colors = defaultTextFieldColors(),
        visualTransformation = if (isPassword) PasswordVisualTransformation()
        else VisualTransformation.Companion.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = isError
    )
}


@Composable
fun ClickableRow(onClick: () -> Unit, content: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun BasicCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, bottom = 15.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 15.dp)
                .animateContentSize(),
            content = content
        )
    }
}

@Composable
fun ExpandableCard(title: String, content: @Composable () -> Unit) {
    val expanded by remember { mutableStateOf(false) }
    val transactionState = remember {
        MutableTransitionState(expanded)
            .apply {
                targetState = expanded
            }
    }
    val transaction = updateTransition(transactionState, label = "")
    BasicCard {
        Surface(
            color = MaterialTheme.colors.surface
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        transactionState.targetState = !transaction.currentState
                    }),
            ) {
                Text(text = title, style = MaterialTheme.typography.h2)
                val iconId =
                    if (!transaction.currentState) R.drawable.ic_baseline_expand_more_24 else R.drawable.ic_baseline_expand_less_24
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconId),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        }
        if (transactionState.currentState) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun BasicDivider() {
    Divider(modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp))
}

@Composable
fun BasicLazyColumn(
    content: @Composable LazyItemScope.() -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .padding(start = 20.dp, end = 20.dp)
    ) {
        item(content = {
            Spacer(modifier = Modifier.padding(10.dp))
            content()
            Spacer(modifier = Modifier.padding(100.dp))
        })
    }
}

@Preview
@Composable
fun ExpandableCardPreview() {
    SmorkTheme {
        ExpandableCard(title = "Tasks") {
            TaskListWithCheckbox(tasks = listOf())
        }
    }
}
