package de.mreuter.freelancer.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.mreuter.freelancer.TopBar
import de.mreuter.freelancer.ui.navigation.BottomNavigationBar
import de.mreuter.freelancer.ui.theme.ButtonBackgroundColor
import de.mreuter.freelancer.ui.theme.ButtonContentColor
import de.mreuter.freelancer.ui.theme.Typography
import de.mreuter.freelancer.ui.theme.defaultTextFieldColors

@Composable
fun BasicListItem(topic: String? = null, description: String? = null) {
    Column {
        if(topic != null)
            Text(text = topic, style = Typography.subtitle1)
        Spacer(modifier = Modifier.padding(2.dp))
        if(description != null)
            Text(text = description, style = Typography.subtitle2)
        Spacer(modifier = Modifier.padding(0.dp))
    }
}

@Composable
fun ClickableListItem(topic: String? = null, clickableText: String? = null, action: (Int) -> Unit){
    Column {
        if(topic != null)
            Text(text = topic, style = Typography.subtitle1)
        Spacer(modifier = Modifier.padding(0.dp))
        if(clickableText != null)
            ClickableText(
                text = AnnotatedString(clickableText),
                style = Typography.subtitle2,
                modifier = Modifier
                    .padding(horizontal = 1.dp, vertical = 4.dp),
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
fun BasicCard(content: @Composable () -> Unit){
    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        content = content
    )
}

@Composable
fun BasicDivider(){
    Divider(modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp))
}

@Composable
fun BasicScaffoldWithLazyColumn(navController: NavController?, content: @Composable () -> Unit){
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        LazyColumn(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            item {
                content
            }
        }
    }
}