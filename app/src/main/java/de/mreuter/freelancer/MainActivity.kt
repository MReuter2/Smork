package de.mreuter.freelancer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.mreuter.freelancer.backend.*
import de.mreuter.freelancer.ui.navigation.Screens
import de.mreuter.freelancer.ui.screens.*
import de.mreuter.freelancer.ui.theme.*
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreelancerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = BackgroundColor
                ) {
                    MainContent()
                }
            }
        }
    }
}

val SIGN_IN = "signIn"
val SIGN_UP = "signUp"
val JOIN_COMPANY = "joinCompany"
val HOME = "home"
val COMPANY = {companyID: UUID? ->
    if(companyID == null) "company/{companyID}" else "company/{$companyID}"
}
val CLIENTS = "clients"
val CLIENT_PROFILE = {clientID: UUID?, editable: Boolean? ->
    if(clientID == null) "client/{clientID}?edit={editable}"
    else "client/{$clientID}?edit={$editable}"
}
val NEW_CLIENT = "newclient"
val WORKER_PROFILE = {workerID: UUID?, editable: Boolean? ->
    if(workerID == null) "worker/{workerID}?edit={editable}"
    else "worker/{$workerID}?edit={$editable}"
}
val OWNER_PROFILE = {ownerID: UUID?, editable: Boolean? ->
    if(ownerID == null) "owner/{ownerID}?edit={editable}"
    else "owner/{$ownerID}?edit={$editable}"
}
val PROJECTS = "projects"
val NEW_PROJECT = {clientID: UUID? ->
    if(clientID == null) "newproject?client={clientID}" else "newproject?client={$clientID}"
}

val stateHolder = Stateholder()

fun setUser(person: Person){
    stateHolder.user = person
}

@Composable
fun MainContent() {
    val listItems = listOf(
        Screens.Home,
        Screens.Projects,
        Screens.Company,
        Screens.Clients
    )

    val navController = rememberNavController()

    Surface() {
        NavHost(navController = navController, startDestination = SIGN_UP) {
            composable(SIGN_IN) { SignIn(navController = navController) }
            composable(SIGN_UP) { SignUp(navController = navController) }
            composable(JOIN_COMPANY) { JoinCompany(navController = navController) }
            composable(HOME) { Home(navController = navController) }
            composable(COMPANY(null)) { YourCompany() }
            composable(CLIENTS) { Clients(navController = navController) }
            composable(NEW_CLIENT) { NewClient(navController = navController) }
            composable(CLIENT_PROFILE(null, null),
                arguments = listOf(
                    navArgument("clientID") {
                        type = NavType.StringType
                    },
                    navArgument("edit") {
                        type = NavType.BoolType
                    }
                )
            ) {
                val clientID = UUID.fromString(it.arguments?.getString("clientID"))
                val edit = it.arguments?.getBoolean("edit")
                if (clientID != null) {
                    ClientProfile(stateHolder.getClientByID(clientID) ?: throw RuntimeException("There is no Client with ID: $clientID"), edit ?: false)
                }
            }
            /*composable(WORKER_PROFILE(null,null),
                arguments = listOf(
                    navArgument("workerID") {
                        type = NavType.StringType
                    },
                    navArgument("edit") {
                        type = NavType.BoolType
                    }
                )
            ) {
                val workerID = UUID.fromString(it.arguments?.getString("workerID"))
                val edit = it.arguments?.getBoolean("edit")
                if (workerID != null) {
                    WorkerView(stateHolder?., edit ?: false)
                }
            }
            composable(OWNER_PROFILE(null, null),
                arguments = listOf(
                    navArgument("ownerID") {
                        type = NavType.IntType
                    },
                    navArgument("edit") {
                        type = NavType.BoolType
                    }
                )
            ) {
                val ownerID = UUID.fromString(it.arguments?.getString("ownerID"))
                val edit = it.arguments?.getBoolean("edit")
                if (ownerID != null) {
                    OwnerView(personService.findByID(ownerID) as Owner, edit ?: false)
                }
            }*/
            composable(PROJECTS){
                Projects(navController)
            }
            composable(NEW_PROJECT(null),
                arguments = listOf(
                    navArgument("clientID"){
                        type = NavType.StringType
                    }
                )
            ){
                val clientID = UUID.fromString(it.arguments?.getString("clientID"))
                NewProject(navController = navController, clientID)
            }
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                "Smork",
                color = White
            )
        },
        backgroundColor = Purple
    )
}

@Composable
fun BottomBar() {
    BottomAppBar(
        backgroundColor = Purple,
        content = {}
    )
}

@Preview
@Composable
fun PreviewTopBar() {
    FreelancerTheme {
        TopBar()
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    FreelancerTheme {
        BottomBar()
    }
}

@Preview
@Composable
fun MainContentPreview() {
    FreelancerTheme {
        MainContent()
    }
}