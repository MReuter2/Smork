package de.mreuter.freelancer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.mreuter.freelancer.backend.EmailAddress
import de.mreuter.freelancer.backend.Person
import de.mreuter.freelancer.ui.theme.*


@Composable
fun SignIn(navController: NavController? = null){
    val passwordValue = remember{ mutableStateOf(TextFieldValue())}
    val emailValue = remember{ mutableStateOf(String())}
    Column (
        modifier = Modifier
            .padding(all = 40.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    ){
        Column {
            Text(
                text = "Sign In",
                style = Typography.h1,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "Sign in to continue",
                style = Typography.subtitle1,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = emailValue.value,
            onValueChange = {
                emailValue.value = it
            },
            label = { Text(text = "Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = defaultTextFieldColors(),
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            value = passwordValue.value,
            onValueChange = {
                passwordValue.value = it
            },
            label = { Text(text = "Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = defaultTextFieldColors()
        )
        Spacer(modifier = Modifier.padding(15.dp))
        OutlinedButton(
            shape = CircleShape,
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = ButtonBackgroundColor,
                contentColor = ButtonContentColor
            ),
            onClick = {
                val currentUser = stateHolder.findPersonByEmail(EmailAddress(emailValue.value))
                if(currentUser != null) {
                    setUser(currentUser)
                    if(stateHolder.usersCompany() != null) {
                        navController?.navigate(HOME)
                    }else{
                        navController?.navigate(JOIN_COMPANY)
                    }
                }
                      },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
        ){
            Text(
                text = "Login",
                style = TextStyle(color = ButtonContentColor)
            )
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Text(
            text = "Dont have an Account?",
            style = Typography.body1,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        ClickableText(
            text = AnnotatedString("Sign up"),
            onClick = { navController?.navigate(SIGN_UP) },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally),
            style = TextStyle(color = LinkBlue)
        )
    }
}

@Composable
fun SignUp(navController: NavController? = null){
    val firstnameValue = remember{ mutableStateOf(TextFieldValue())}
    val lastnameValue = remember{ mutableStateOf(TextFieldValue())}
    val passwordValue = remember{ mutableStateOf(TextFieldValue())}
    val emailValue = remember{ mutableStateOf(TextFieldValue())}
    Column (
        modifier = Modifier
            .padding(all = 40.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    ){
        Column {
            Text(
                text = "Sign Up",
                style = Typography.h1,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "Sign up to join",
                style = Typography.subtitle1,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = firstnameValue.value,
            onValueChange = {
                firstnameValue.value = it
            },
            label = { Text(text = "First Name") },
            maxLines = 1,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = defaultTextFieldColors()
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            value = lastnameValue.value,
            onValueChange = {
                lastnameValue.value = it
            },
            label = { Text(text = "Last Name") },
            maxLines = 1,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = defaultTextFieldColors()
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            value = emailValue.value,
            onValueChange = {
                emailValue.value = it
            },
            label = { Text(text = "Email") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = defaultTextFieldColors()
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            value = passwordValue.value,
            onValueChange = {
                passwordValue.value = it
            },
            label = { Text(text = "Password") },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = defaultTextFieldColors()
        )
        Spacer(modifier = Modifier.padding(15.dp))
        OutlinedButton(
            shape = CircleShape,
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = ButtonBackgroundColor,
                contentColor = ButtonContentColor
            ),
            onClick = { navController?.navigate(JOIN_COMPANY) },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
        ){
            Text(
                text = "Sign up",
                style = TextStyle(color = ButtonContentColor)
            )
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Text(
            text = "Already have an Account?",
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            style = Typography.body1
        )
        ClickableText(
            text = AnnotatedString("Sign in"),
            onClick = { navController?.navigate(SIGN_IN) },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally),
            style = TextStyle(color = LinkBlue)
        )
    }
}

@Composable
fun JoinCompany(navController: NavController? = null){
    val companyIdValue = remember{ mutableStateOf(TextFieldValue())}
    val nameValue = remember{ mutableStateOf(TextFieldValue())}
    val descriptionValue = remember{ mutableStateOf(TextFieldValue())}

    Column (
        modifier = Modifier
            .padding(all = 40.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    ){
        Column {
            Text(
                text = "Join a company",
                style = Typography.h1,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "Join a company as worker",
                style = Typography.subtitle1,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = companyIdValue.value,
            onValueChange = {
                companyIdValue.value = it
            },
            label = { Text(text = "Company-ID") },
            maxLines = 1,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = defaultTextFieldColors()
        )
        Spacer(modifier = Modifier.padding(15.dp))
        OutlinedButton(
            shape = CircleShape,
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = ButtonBackgroundColor,
                contentColor = ButtonContentColor
            ),
            onClick = { navController?.navigate(HOME) },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
        ){
            Text(
                text = "Join",
                style = TextStyle(color = ButtonContentColor)
            )
        }
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                ){
            Column (verticalArrangement = Arrangement.Center){
                Text("")
                Divider(modifier = Modifier)
                Text("")
            }
            Text(
                text = "or",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 5.dp, end = 5.dp)
            )
            Column (verticalArrangement = Arrangement.Center){
                Text("")
                Divider(
                    modifier = Modifier
                )
                Text("")
            }
        }
        Column {
            Text(
                text = "Create a company",
                style = Typography.h1,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "Create a company as owner",
                style = Typography.subtitle1,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = nameValue.value,
            onValueChange = {
                nameValue.value = it
            },
            label = { Text(text = "Name") },
            maxLines = 1,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = defaultTextFieldColors()
        )
        Spacer(modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = descriptionValue.value,
            onValueChange = {
                descriptionValue.value = it
            },
            label = { Text(text = "Description") },
            maxLines = 1,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = defaultTextFieldColors()
        )
        Spacer(modifier = Modifier.padding(15.dp))
        OutlinedButton(
            shape = CircleShape,
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = ButtonBackgroundColor,
                contentColor = ButtonContentColor
            ),
            onClick = { navController?.navigate(HOME) },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
        ){
            Text(
                text = "Create",
                style = TextStyle(color = ButtonContentColor)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSignIn(){
    FreelancerTheme {
        SignIn()
    }
}

@Preview
@Composable
fun PreviewSignUp(){
    FreelancerTheme {
        SignUp()
    }
}

@Preview
@Composable
fun PreviewJoinCompany(){
    FreelancerTheme {
        JoinCompany()
    }
}