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
import androidx.navigation.compose.rememberNavController
import de.mreuter.smork.backend.*
import de.mreuter.smork.stateHolder
import de.mreuter.smork.ui.elements.BasicLazyColumn
import de.mreuter.smork.ui.elements.BasicOutlinedTextField
import de.mreuter.smork.ui.elements.PrimaryButton
import de.mreuter.smork.ui.navigation.Screen
import de.mreuter.smork.ui.theme.*
import java.util.*


@Composable
fun SignIn(
    navigateToSignUp: () -> Unit = {},
    navigateToHome: () -> Unit = {}
) {
    val passwordValue = remember { mutableStateOf(String()) }
    val emailValue = remember { mutableStateOf(String()) }
    BasicLazyColumn {
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
            onValueChange = { emailValue.value = it },
            keyboardType = KeyboardType.Email
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
                    navigateToHome()
                }
            }
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Dont have an Account?",
                style = Typography.body1,
                modifier = Modifier
            )
            ClickableText(
                text = AnnotatedString("Sign up"),
                onClick = { navigateToSignUp() },
                style = TextStyle(color = LinkBlue)
            )
        }
    }
}

@Composable
fun SignUp(
    navigateToSignIn: () -> Unit = {},
    navigateToJoinCompany: (Fullname, EmailAddress, Long) -> Unit = { _, _, _ -> }
) {
    val firstnameValue = remember { mutableStateOf(String()) }
    val lastnameValue = remember { mutableStateOf(String()) }
    val passwordValue = remember { mutableStateOf(String()) }
    val emailValue = remember { mutableStateOf(String()) }
    val phonenumberValue = remember { mutableStateOf(String()) }
    BasicLazyColumn {
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
                if (it.toLongOrNull() != null)
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
            navigateToJoinCompany(
                Fullname(firstnameValue.value, lastnameValue.value),
                EmailAddress(emailValue.value),
                phonenumberValue.value.toLongOrNull()
                    ?: throw RuntimeException("False Number")
            )
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Already have an Account?",
                style = Typography.body1
            )
            ClickableText(
                text = AnnotatedString("Sign in"),
                onClick = { navigateToSignIn() },
                style = TextStyle(color = LinkBlue)
            )
        }
    }
}

@Composable
fun JoinCompany(
    fullname: Fullname,
    emailAddress: EmailAddress?,
    phoneNumber: Long?,
    navigateToHome: () -> Unit = {}
) {
    val companyIdValue = remember { mutableStateOf(String()) }
    val nameValue = remember { mutableStateOf(String()) }
    val descriptionValue = remember { mutableStateOf(String()) }

    BasicLazyColumn {
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
            if (company != null) {
                val worker = Worker(fullname, emailAddress, phoneNumber)
                company.addWorker(worker)
                stateHolder.saveCompany(company)
                stateHolder.user = worker
                navigateToHome()
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
                fullname,
                emailAddress
            )
            stateHolder.user = owner
            newCompany.addOwner(owner)
            stateHolder.saveCompany(newCompany)
            navigateToHome()
        }

        Spacer(modifier = Modifier.padding(150.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignIn() {
    FreelancerTheme {
        SignIn()
    }
}

@Preview
@Composable
fun PreviewSignUp() {
    FreelancerTheme {
        SignUp()
    }
}

@Preview
@Composable
fun PreviewJoinCompany() {
    val owner = exampleOwner[0]
    FreelancerTheme {
        JoinCompany(owner.fullname, owner.email, owner.phonenumber)
    }
}