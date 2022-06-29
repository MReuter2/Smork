package de.mreuter.smork.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.mreuter.smork.*
import de.mreuter.smork.backend.Owner
import de.mreuter.smork.backend.TestData
import de.mreuter.smork.backend.Worker
import de.mreuter.smork.ui.elements.BasicCard
import de.mreuter.smork.ui.elements.ClickableListItem
import de.mreuter.smork.ui.navigation.*
import de.mreuter.smork.ui.theme.FreelancerTheme
import de.mreuter.smork.ui.theme.Typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun YourCompany(navController: NavController? = null){
    Scaffold(
        topBar = { TopBar() },
        bottomBar = {
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            ) {
                item {
                    BasicCard{
                            Column {
                                Text(
                                    text = "Your Company GmbH",
                                    style = Typography.h1,
                                )
                                Spacer(modifier = Modifier.padding(2.dp))
                                Text(
                                    text = "Your company description",
                                    style = Typography.subtitle2,
                                    modifier = Modifier.padding(horizontal = 5.dp)
                                )
                            }
                            Text(
                                text = "Owner",
                                style = Typography.subtitle1,
                                modifier = Modifier.padding(top = 25.dp, bottom = 10.dp)
                            )
                            stateHolder.getOwner().forEach { owner ->
                                ClickableListItem(
                                    clickableText = "${owner.fullName}",
                                    action = {
                                        navController?.navigate(OWNER_PROFILE(owner.uuid, false))
                                    }
                                )
                            }
                            Text(
                                text = "Worker",
                                style = Typography.subtitle1,
                                modifier = Modifier.padding(top = 25.dp, bottom = 10.dp)
                            )
                                stateHolder.getWorker().forEach { worker ->
                                    ClickableListItem(
                                        clickableText = "${worker.fullName}",
                                        action = {
                                            navController?.navigate(WORKER_PROFILE(worker.uuid, false))
                                        })
                                }
                            Spacer(modifier = Modifier.padding(0.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun WorkerView(worker: Worker, edit: Boolean = false){

}

@Composable
fun OwnerView(owner: Owner, edit: Boolean = false){

}


@Preview
@Composable
fun PreviewYourCompany(){
    TestData()
    FreelancerTheme {
        YourCompany()
    }
}