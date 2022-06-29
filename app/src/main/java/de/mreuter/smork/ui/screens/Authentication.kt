package de.mreuter.smork.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.mreuter.smork.backend.*
import de.mreuter.smork.stateHolder
import de.mreuter.smork.ui.elements.BasicOutlinedTextField
import de.mreuter.smork.ui.elements.PrimaryButton
import de.mreuter.smork.ui.navigation.HOME
import de.mreuter.smork.ui.navigation.JOIN_COMPANY
import de.mreuter.smork.ui.navigation.SIGN_IN
import de.mreuter.smork.ui.navigation.SIGN_UP
import de.mreuter.smork.ui.theme.*
import java.util.*


@Composable
fun SignIn(navController: NavController? = null){
    val passwordValue = remember{ mutableStateOf(String())}
    val emailValue = remember{ mutableStateOf(String())}
    Scaffold {
        LazyColumn(
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            item {
                Spacer(modifier = Modifier.padding(20.dp))
                Column {
                    Text(
                        text = "Sign In",
                        style = Typography.h1,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Sign in to continue",
                        style = Typography.subtitle2,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                BasicOutlinedTextField(
                    label = "Email",
                    value = emailValue.value,
                    onValueChange = { emailValue.value = it }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                BasicOutlinedTextField(
                    label = "Password",
                    value = passwordValue.value,
                    onValueChange = { passwordValue.value = it },
                    isPassword = true
                )
                Spacer(modifier = Modifier.padding(15.dp))
                PrimaryButton(label = "Login") {
                    val currentUser =
                        stateHolder.findPersonByEmail(EmailAddress(emailValue.value))
                    if (currentUser != null) {
                        stateHolder.user = currentUser
                        if (stateHolder.usersCompany() != null) {
                            navController?.navigate(HOME)
                        } else {
                            navController?.navigate(JOIN_COMPANY)
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(15.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "Dont have an Account?",
                        style = Typography.body1,
                        modifier = Modifier
                    )
                    ClickableText(
                        text = AnnotatedString("Sign up"),
                        onClick = { navController?.navigate(SIGN_UP) },
                        style = TextStyle(color = LinkBlue)
                    )
                }
                Spacer(modifier = Modifier.padding(150.dp))
            }
        }
    }
}

private var emailAddress: EmailAddress? = null
private var fullname: Fullname? = null
private var phoneNumber: Long? = null

@Composable
fun SignUp(navController: NavController? = null){
    val firstnameValue = remember{ mutableStateOf(String())}
    val lastnameValue = remember{ mutableStateOf(String())}
    val passwordValue = remember{ mutableStateOf(String())}
    val emailValue = remember{ mutableStateOf(String())}
    val phonenumberValue = remember { mutableStateOf(String()) }
    Scaffold {
        LazyColumn(
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            item {
                Spacer(modifier = Modifier.padding(20.dp))
                Column {
                    Text(
                        text = "Sign Up",
                        style = Typography.h1,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Sign up to join",
                        style = Typography.subtitle2,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                BasicOutlinedTextField(
                    label = "First Name",
                    value = firstnameValue.value,
                    onValueChange = {
                        if (firstnameValue.value.trim() != it.trim())
                            firstnameValue.value = it
                    }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                BasicOutlinedTextField(
                    label = "Last Name",
                    value = lastnameValue.value,
                    onValueChange = {
                        if (lastnameValue.value.trim() != it.trim())
                            lastnameValue.value = it
                    }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                BasicOutlinedTextField(
                    label = "Email",
                    value = emailValue.value,
                    onValueChange = {
                        if (emailValue.value.trim() != it.trim())
                            emailValue.value = it
                    },
                    keyboardType = KeyboardType.Email
                )
                Spacer(modifier = Modifier.padding(5.dp))
                BasicOutlinedTextField(
                    label = "Phonenumber",
                    value = phonenumberValue.value,
                    onValueChange = {
                        if(it.toLongOrNull() != null)
                            phonenumberValue.value = it
                    },
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.padding(5.dp))
                BasicOutlinedTextField(
                    label = "Password",
                    value = passwordValue.value,
                    onValueChange = {
                        if (passwordValue.value.trim() != it.trim())
                            passwordValue.value = it
                    },
                    isPassword = true
                )
                Spacer(modifier = Modifier.padding(15.dp))
                PrimaryButton(label = "Sign up") {
                    fullname = Fullname(firstnameValue.value, lastnameValue.value)
                    emailAddress = EmailAddress(emailValue.value)
                    navController?.navigate(JOIN_COMPANY)
                }
                Spacer(modifier = Modifier.padding(15.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "Already have an Account?",
                        style = Typography.body1
                    )
                    ClickableText(
                        text = AnnotatedString("Sign in"),
                        onClick = { navController?.navigate(SIGN_IN) },
                        style = TextStyle(color = LinkBlue)
                    )
                }
                Spacer(modifier = Modifier.padding(150.dp))
            }
        }
    }
}

@Composable
fun JoinCompany(navController: NavController? = null){
    val companyIdValue = remember{ mutableStateOf(String())}
    val nameValue = remember{ mutableStateOf(String())}
    val descriptionValue = remember{ mutableStateOf(String())}

    Scaffold{
        LazyColumn(
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            item {
                Spacer(modifier = Modifier.padding(20.dp))
                Column {
                    Text(
                        text = "Join a company",
                        style = Typography.h1,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Join a company as worker",
                        style = Typography.subtitle2,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                BasicOutlinedTextField(
                    label = "Company-ID",
                    value = companyIdValue.value,
                    onValueChange = { companyIdValue.value = it }
                )
                Spacer(modifier = Modifier.padding(15.dp))
                PrimaryButton(label = "Join") {
                    val company = stateHolder.getCompanyByID(UUID.fromString(companyIdValue.value))
                    if(company != null) {
                        val worker = Worker(fullname ?: throw RuntimeException(), emailAddress, phoneNumber)
                        company.addWorker(worker)
                        stateHolder.saveCompany(company)
                        stateHolder.user = worker
                        navController?.navigate(HOME)
                    }
                }
                Divider(modifier = Modifier.padding(top = 40.dp, bottom = 20.dp))
                Column {
                    Text(
                        text = "Create a company",
                        style = Typography.h1,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "Create a company as owner",
                        style = Typography.subtitle2,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                BasicOutlinedTextField(
                    label = "Name",
                    value = nameValue.value,
                    onValueChange = { nameValue.value = it }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                BasicOutlinedTextField(
                    label = "Description",
                    value = descriptionValue.value,
                    onValueChange = { descriptionValue.value = it }
                )
                Spacer(modifier = Modifier.padding(15.dp))
                PrimaryButton(label = "Create") {
                    val newCompany = Company(nameValue.value, descriptionValue.value)
                    val owner = Owner(
                        fullname ?: throw RuntimeException("Cant get in here"),
                        emailAddress
                    )
                    stateHolder.user = owner
                    newCompany.addOwner(owner)
                    stateHolder.saveCompany(newCompany)
                    navController?.navigate(HOME)
                }

                Spacer(modifier = Modifier.padding(150.dp))
            }
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