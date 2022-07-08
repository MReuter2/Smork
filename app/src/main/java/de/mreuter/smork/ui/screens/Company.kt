package de.mreuter.smork.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.mreuter.smork.*
import de.mreuter.smork.backend.*
import de.mreuter.smork.ui.elements.BasicCard
import de.mreuter.smork.ui.elements.BasicLazyColumn
import de.mreuter.smork.ui.elements.ClickableListItem
import de.mreuter.smork.ui.navigation.*
import de.mreuter.smork.ui.theme.FreelancerTheme
import de.mreuter.smork.ui.theme.Typography

@Composable
fun YourCompany(company: Company) {
    BasicLazyColumn {
        BasicCard {
            Column {
                Text(
                    text = company.name,
                    style = Typography.h1,
                )
                Spacer(modifier = Modifier.padding(2.dp))
                Text(
                    text = company.description,
                    style = Typography.subtitle2,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }
            Text(
                text = "Owner",
                style = Typography.subtitle1,
                modifier = Modifier.padding(top = 25.dp, bottom = 10.dp)
            )
            company.owner.forEach { owner ->
                ClickableListItem(
                    clickableText = "${owner.fullName}",
                    action = {
                        /*TODO: Something to watch ownerdetails*/
                    }
                )
            }
            Text(
                text = "Worker",
                style = Typography.subtitle1,
                modifier = Modifier.padding(top = 25.dp, bottom = 10.dp)
            )
            company.worker.forEach { worker ->
                ClickableListItem(
                    clickableText = "${worker.fullName}",
                    action = {
                        /*TODO: Something to watch workerdetails*/
                    })
            }
        }
    }
}

@Composable
fun WorkerView(worker: Worker, edit: Boolean = false) {

}

@Composable
fun OwnerView(owner: Owner, edit: Boolean = false) {

}


@Preview
@Composable
fun PreviewYourCompany() {
    TestData()
    val company = exampleCompanies[0]
    FreelancerTheme {
        YourCompany(company)
    }
}