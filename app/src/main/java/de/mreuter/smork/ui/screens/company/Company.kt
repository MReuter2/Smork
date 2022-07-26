package de.mreuter.smork.ui.screens.company

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.company.domain.Company
import de.mreuter.smork.backend.owner.domain.Owner
import de.mreuter.smork.backend.worker.application.WorkerEntity
import de.mreuter.smork.backend.worker.domain.Worker
import de.mreuter.smork.exampleCompanies
import de.mreuter.smork.exampleOwner
import de.mreuter.smork.exampleWorker
import de.mreuter.smork.ui.elements.*
import de.mreuter.smork.ui.theme.SmorkTheme

@Composable
fun YourCompany(
    company: Company,
    owner: List<Owner>,
    worker: List<Worker>,
    onWorkerClick: (Worker) -> Unit = {},
    onOwnerClick: (Owner) -> Unit = {},
    onClickOwnerAddButton: () -> Unit = {},
    onClickWorkerAddButton: () -> Unit = {},
    onCompanyEdit: (Company) -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    BasicScaffold(
        bottomBar = { bottomBar() },
        topBarTitle = "Your Company",
        trailingAppBarIcons = {
            IconButton(
                onClick = {
                    onCompanyEdit(company)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_edit_24),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ){
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
            ){
                Text(
                    text = "Owner",
                    style = MaterialTheme.typography.subtitle1,
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_add_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { onClickOwnerAddButton() }
                        .size(20.dp),
                    tint = MaterialTheme.colors.onSurface
                )
            }
            BasicCard {
                val sortedOwner = owner
                    .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
                sortedOwner.forEach { currentOwner ->
                    ClickableListItem(
                        subtitle = "${currentOwner.fullname}",
                        action = {
                            onOwnerClick(currentOwner)
                        }
                    )
                    if(currentOwner != sortedOwner.last())
                        BasicDivider()
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
            ){
                Text(
                    text = "Worker",
                    style = MaterialTheme.typography.subtitle1,
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_add_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { onClickWorkerAddButton() }
                        .size(20.dp),
                    tint = MaterialTheme.colors.onSurface
                )
            }
            BasicCard {
                val sortedWorker = worker.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.fullname.lastname })
                sortedWorker.forEach { currentWorker ->
                    ClickableListItem(
                        subtitle = "${currentWorker.fullname}",
                        action = {
                            onWorkerClick(currentWorker)
                        }
                    )
                    if(currentWorker != sortedWorker.last())
                        BasicDivider()
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewYourCompany() {
    val company = exampleCompanies[0]
    SmorkTheme {
        YourCompany(
            company,
            exampleOwner,
            exampleWorker
        ) { BottomNavigationBar() }
    }
}