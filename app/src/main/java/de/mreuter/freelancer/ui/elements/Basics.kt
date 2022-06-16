package de.mreuter.freelancer.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import de.mreuter.freelancer.TopBar
import de.mreuter.freelancer.ui.ExposedDropMenuStateHolder
import de.mreuter.freelancer.ui.navigation.BottomNavigationBar
import de.mreuter.freelancer.ui.rememberExposedMenuStateHolder
import de.mreuter.freelancer.ui.theme.ButtonBackgroundColor
import de.mreuter.freelancer.ui.theme.ButtonContentColor
import de.mreuter.freelancer.ui.theme.Typography
import de.mreuter.freelancer.ui.theme.defaultTextFieldColors

@Composable
fun BasicListItem(topic: String? = null, description: String? = null) {
    Column {
        if(topic != null)
            Text(text = topic, style = Typography.subtitle1)
        if(description != null)
            Text(
                text = description,
                style = Typography.subtitle2,
                modifier = Modifier.padding(start = 1.dp, top = 4.dp)
            )
    }
}

@Composable
fun ClickableListItem(topic: String? = null, clickableText: String? = null, action: (Int) -> Unit){
    Column {
        if(topic != null)
            Text(text = topic, style = Typography.subtitle1)
        if(clickableText != null)
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
fun BasicOutlinedTextField(label: String, value: String, onValueChange: (String) -> Unit, isPassword: Boolean = false, keyboardType: KeyboardType = KeyboardType.Text){
    if(isPassword) {
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
    }else{
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
fun PrimaryButton(label: String, onClick: () -> Unit){
    OutlinedButton(
        shape = CircleShape,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = ButtonBackgroundColor,
            contentColor = ButtonContentColor,
        ),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = TextStyle(color = ButtonContentColor)
        )
    }
}

@Composable
fun BasicCard(content: @Composable ColumnScope.() -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 20.dp)
    ){
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 15.dp),
            content = content
        )
    }
}

@Composable
fun BasicDivider(){
    Divider(modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp))
}

@Composable
fun BasicScaffoldWithLazyColumn(navController: NavController?, content: @Composable LazyItemScope.() -> Unit){
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        LazyColumn(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            item (content = content)
        }
    }
}

class DropDown(val dropDownItems: List<Any>){
    var exposedMenuStateHolder: ExposedDropMenuStateHolder<Any> = ExposedDropMenuStateHolder(dropDownItems)

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