package de.mreuter.smork.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import de.mreuter.smork.*
import de.mreuter.smork.backend.*
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.navigation.*
import de.mreuter.smork.ui.theme.FreelancerTheme
import de.mreuter.smork.ui.theme.Typography

@Composable
fun YourCompany(company: Company, bottomBar: @Composable () -> Unit) {
    BasicScaffold(bottomBar = { bottomBar() }, topBarTitle = "Your Company"){
        BasicLazyColumn {
            BasicCard {
                Column {
                    Text(
                        text = company.name,
                        style = MaterialTheme.typography.h1,
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = company.description,
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    )
                }
            }
            Text(
                text = "Owner",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 15.dp)
            )
            BasicCard {
                val sortedOwner = company.owner.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
                sortedOwner.forEach { currentOwner ->
                    ClickableListItem(
                        subtitle = "${currentOwner.fullName}",
                        action = {
                            /*TODO: Something to watch ownerdetails*/
                        }
                    )
                    if(currentOwner != sortedOwner.last())
                        BasicDivider()
                }
            }
            Text(
                text = "Worker",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 15.dp)
            )
            BasicCard {
                val sortedWorker = company.worker.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
                sortedWorker.forEach { currentWorker ->
                    ClickableListItem(
                        subtitle = "${currentWorker.fullName}",
                        action = {
                            /*TODO: Something to watch workerdetails*/
                        }
                    )
                    if(currentWorker != sortedWorker.last())
                        BasicDivider()
                }
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
            YourCompany(company) { BottomNavigationBar(rememberNavController()) }
    }
}