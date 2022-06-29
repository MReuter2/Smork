package de.mreuter.smork.ui.elements

import android.os.Build
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import de.mreuter.smork.R
import de.mreuter.smork.TopBar
import de.mreuter.smork.backend.TestData
import de.mreuter.smork.backend.exampleProjects
import de.mreuter.smork.ui.ExposedDropMenuStateHolder
import de.mreuter.smork.ui.navigation.BottomNavigationBar
import de.mreuter.smork.ui.rememberExposedMenuStateHolder
import de.mreuter.smork.ui.screens.TaskListWithCheckbox
import de.mreuter.smork.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun BasicListItem(topic: String? = null, description: String? = null) {
    Column {
        if (topic != null)
            Text(text = topic, style = Typography.subtitle1)
        if (description != null)
            Text(
                text = description,
                style = Typography.subtitle2,
                modifier = Modifier.padding(start = 1.dp, top = 4.dp)
            )
    }
}

@Composable
fun ClickableListItem(topic: String? = null, clickableText: String? = null, action: (Int) -> Unit) {
    Column {
        if (topic != null)
            Text(text = topic, style = Typography.subtitle1)
        if (clickableText != null)
            ClickableText(
                text = AnnotatedString(clickableText),
                style = Typography.subtitle2,
                modifier = Modifier
                    .padding(start = 1.dp, top = 4.dp),
                onClick = action
            )
    }
}

@Composable
fun BasicOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    if (isPassword) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            colors = defaultTextFieldColors(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            colors = defaultTextFieldColors(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Composable
fun PrimaryButton(label: String, onClick: () -> Unit) {
    OutlinedButton(
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = ButtonBackgroundColor,
            contentColor = ButtonContentColor,
        ),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = label,
            style = TextStyle(color = ButtonContentColor)
        )
    }
}

@Composable
fun SecondaryButton(label: String, onClick: () -> Unit) {
    OutlinedButton(
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Purple
        ),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        border = BorderStroke(1.dp, Purple),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 10.dp,
            pressedElevation = 15.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = label,
            style = TextStyle(color = Black07)
        )
    }
}

@Preview
@Composable
fun ButtonPreview(){
    FreelancerTheme {
        Column {
            PrimaryButton(label = "Test Button") {

            }
            SecondaryButton(label = "Test Button") {

            }
        }
    }
}

@Composable
fun BasicCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp),
        shape = Shapes.medium,
        elevation = 5.dp
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
    val transactionState = remember{
        MutableTransitionState(expanded)
            .apply {
                targetState = expanded
            }
    }
    val transaction = updateTransition(transactionState, label = "")
    BasicCard {
        Surface {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        transactionState.targetState = !transaction.currentState
                    }),
            ){
                Text(text = title, style = Typography.h2)
                val iconId = if(!transaction.currentState) R.drawable.ic_baseline_expand_more_24 else R.drawable.ic_baseline_expand_less_24
                Image(
                    imageVector = ImageVector.vectorResource(id = iconId),
                    contentDescription = null
                )
            }
        }
        if(transactionState.currentState){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ){
                content()
            }
        }
    }
}

@Preview
@Composable
fun ExpandableCardPreview(){
    TestData()
    FreelancerTheme {
        ExpandableCard(title = "Tasks") {
            TaskListWithCheckbox(tasks = exampleProjects[0].tasks)
        }
    }
}

@Composable
fun BasicDivider() {
    Divider(modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp))
}

@Composable
fun BasicScaffoldWithLazyColumn(
    navController: NavController?,
    content: @Composable LazyItemScope.() -> Unit
) {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        LazyColumn(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            item(content = content)
        }
    }
}

class DropDown(val dropDownItems: List<Any>) {
    var exposedMenuStateHolder: ExposedDropMenuStateHolder<Any> =
        ExposedDropMenuStateHolder(dropDownItems)

    @Composable
    fun DropDownTextfield(label: String) {
        exposedMenuStateHolder = rememberExposedMenuStateHolder(dropDownItems)
        OutlinedTextField(
            value = exposedMenuStateHolder.value,
            onValueChange = {},
            label = { Text(text = label) },
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = exposedMenuStateHolder.icon,
                    contentDescription = null,
                    Modifier.clickable {
                        exposedMenuStateHolder.onEnabled(!(exposedMenuStateHolder.enabled))
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { exposedMenuStateHolder.onSize(it.size.toSize()) }
        )
        DropdownMenu(
            expanded = exposedMenuStateHolder.enabled,
            onDismissRequest = { exposedMenuStateHolder.onEnabled(false) },
            modifier = Modifier.width(with(LocalDensity.current) { exposedMenuStateHolder.size.width.toDp() })
        ) {
            exposedMenuStateHolder.items.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        exposedMenuStateHolder.onSelectedIndex(index)
                        exposedMenuStateHolder.onEnabled(false)
                    }
                ) {
                    Text(text = s)
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(onDateSelected: (LocalDate) -> Unit, onDismissRequest: () -> Unit) {
    val selDate = remember { mutableStateOf(LocalDate.now()) }

    //todo - add strings to resource after POC
    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select date".uppercase(),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = selDate.value.format(DateTimeFormatter.ofPattern("MMM d, YYYY")),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(onDateSelected = {
                selDate.value = it
            })

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    //TODO - hardcode string
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onSecondary
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(selDate.value)
                        onDismissRequest()
                    }
                ) {
                    //TODO - hardcode string
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.onSecondary
                    )
                }

            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendarView(onDateSelected: (LocalDate) -> Unit) {
    // Adds view to Compose
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = {
            CalendarView(ContextThemeWrapper(it, R.style.CalenderViewCustom))
        },
        update = { view ->
            view.minDate = 0
                view.maxDate = 20000000000000

                view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    onDateSelected(
                        LocalDate
                            .now()
                            .withMonth(month + 1)
                            .withYear(year)
                            .withDayOfMonth(dayOfMonth)
                    )
                }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun DatePickerPreview(){
    FreelancerTheme {
        DatePicker(onDateSelected = {}) {

        }
    }
}
