package de.mreuter.smork.ui.company.views

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.backend.company.domain.Company
import de.mreuter.smork.exampleCompanies
import de.mreuter.smork.ui.utils.BasicLazyColumn
import de.mreuter.smork.ui.utils.BasicOutlinedTextField
import de.mreuter.smork.ui.utils.BasicScaffold
import de.mreuter.smork.ui.utils.PrimaryButton
import de.mreuter.smork.ui.theme.SmorkTheme

@Composable
fun CompanyEditing(
    company: Company,
    onCompanySave: (Company) -> Unit = {},
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    val nameValue = remember { mutableStateOf(company.name) }
    val descritpionValue = remember { mutableStateOf(company.description) }

    val isError = remember { mutableStateOf(false) }

    val context = LocalContext.current

    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "Company Editing",
        backNavigation = { backNavigation() }
    ) {
        BasicLazyColumn {
            Spacer(modifier = Modifier.padding(5.dp))
            BasicOutlinedTextField(
                label = "Name",
                value = nameValue.value,
                onValueChange = { nameValue.value = it },
                isError = isError.value && nameValue.value.trim() == ""
            )
            Spacer(modifier = Modifier.padding(5.dp))
            BasicOutlinedTextField(
                label = "Description",
                value = descritpionValue.value,
                onValueChange = { descritpionValue.value = it },
                isError = isError.value && descritpionValue.value == ""
            )
            Spacer(modifier = Modifier.padding(15.dp))
            PrimaryButton(label = "Save") {
                if (nameValue.value.trim() != "" && descritpionValue.value.trim() != "") {
                    val updatedCompany = Company(nameValue.value, descritpionValue.value)
                    onCompanySave(updatedCompany)
                    Toast.makeText(context, "${updatedCompany.name} updated", Toast.LENGTH_LONG).show()
                } else {
                    isError.value = true
                }
            }
        }
    }
}

@Preview
@Composable
fun CompanyEditingPreview(){
    SmorkTheme {
        CompanyEditing(company = exampleCompanies[0]) {}
    }
}