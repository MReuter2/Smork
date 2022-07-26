package de.mreuter.smork.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.mreuter.smork.backend.company.domain.Company
import de.mreuter.smork.backend.owner.application.OwnerEntity
import de.mreuter.smork.backend.owner.domain.Owner
import de.mreuter.smork.backend.worker.application.WorkerEntity
import de.mreuter.smork.backend.worker.domain.Worker
import de.mreuter.smork.ui.elements.*

@Composable
fun YourCompany(
    company: Company,
    owner: List<Owner>,
    worker: List<Worker>,
    bottomBar: @Composable () -> Unit
) {
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
                val sortedOwner = owner
                    .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
                sortedOwner.forEach { currentOwner ->
                    ClickableListItem(
                        subtitle = "${currentOwner.fullname}",
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
                val sortedWorker = worker.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
                sortedWorker.forEach { currentWorker ->
                    ClickableListItem(
                        subtitle = "${currentWorker.fullname}",
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
fun WorkerView(workerEntity: WorkerEntity, edit: Boolean = false) {

}

@Composable
fun OwnerView(ownerEntity: OwnerEntity, edit: Boolean = false) {

}


/*@Preview
@Composable
fun PreviewYourCompany() {
    val company = exampleCompanies[0]
    SmorkTheme {
            YourCompany(
                companyEntity = company,
                ownerEntity = exampleOwnerEntities.filter { it.companyId == company.id },
                workerEntity = exampleWorkerEntities.filter { it.companyId == company.id }
            ) { BottomNavigationBar(rememberNavController()) }
    }
}*/