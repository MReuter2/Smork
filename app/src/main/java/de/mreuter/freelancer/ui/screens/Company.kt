package de.mreuter.freelancer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import de.mreuter.freelancer.*
import de.mreuter.freelancer.backend.Owner
import de.mreuter.freelancer.backend.TestData
import de.mreuter.freelancer.backend.Worker
import de.mreuter.freelancer.ui.elements.ClickableListItem
import de.mreuter.freelancer.ui.theme.FreelancerTheme
import de.mreuter.freelancer.ui.theme.Typography

@Composable
fun YourCompany(navController: NavController? = null){
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar() },
        content = {
            LazyColumn(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(horizontal = 40.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.padding(vertical = 40.dp)
                    ) {
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
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "Owner",
                        style = Typography.subtitle1,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Card {
                        stateHolder.getOwner().forEach { owner ->
                            ClickableListItem(clickableText = "${owner.fullName}", action = {
                                navController?.navigate(OWNER_PROFILE(owner.uuid, false))
                            })
                        }
                    }
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "Worker",
                        style = Typography.subtitle1,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Card{
                        stateHolder.getWorker().forEach { worker ->
                            ClickableListItem(clickableText = "${worker.fullName}", action = {
                                navController?.navigate(WORKER_PROFILE(worker.uuid, false))
                            })
                        }
                    }
                    Spacer(modifier = Modifier.padding(50.dp))
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