package de.mreuter.smork.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.backend.company.application.CompanyEntity
import de.mreuter.smork.backend.company.domain.Company
import de.mreuter.smork.ui.utils.BasicLazyColumn
import de.mreuter.smork.ui.utils.BasicOutlinedTextField
import de.mreuter.smork.ui.utils.PrimaryButton
import de.mreuter.smork.ui.theme.*

@Composable
fun CreateCompany(
    onCompanySave: (Company) -> Unit
) {
    val nameValue = remember { mutableStateOf(String()) }
    val descriptionValue = remember { mutableStateOf(String()) }

    BasicLazyColumn {
        Spacer(modifier = Modifier.padding(10.dp))
        Column {
            Text(
                text = "Create a company",
                style = Typography.h1,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = "Give your company a name and a short description",
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
            val newCompany = Company(name = nameValue.value, description = descriptionValue.value)
            onCompanySave(newCompany)
        }

        Spacer(modifier = Modifier.padding(150.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateCompany() {
    SmorkTheme {
        CreateCompany{}
    }
}