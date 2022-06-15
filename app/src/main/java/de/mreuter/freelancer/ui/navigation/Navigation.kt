package de.mreuter.freelancer.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.mreuter.freelancer.*
import de.mreuter.freelancer.R
import de.mreuter.freelancer.ui.screens.*
import de.mreuter.freelancer.ui.theme.Purple
import de.mreuter.freelancer.ui.theme.White
import java.util.*

val SIGN_IN = "signIn"
val SIGN_UP = "signUp"
val JOIN_COMPANY = "joinCompany"
val HOME = "home"
val COMPANY = {companyID: UUID? ->
    if(companyID == null) "company/{companyID}" else "company/{$companyID}"
}
val CLIENTS = "clients"
val CLIENT_PROFILE = { clientID: UUID?, editable: Boolean? ->
    if(clientID == null) "client/{clientID}?edit={editable}"
    else "client/{$clientID}?edit={$editable}"
}
val NEW_CLIENT = "newclient"
val WORKER_PROFILE = { workerID: UUID?, editable: Boolean? ->
    if(workerID == null) "worker/{workerID}?edit={editable}"
    else "worker/{$workerID}?edit={$editable}"
}
val OWNER_PROFILE = { ownerID: UUID?, editable: Boolean? ->
    if(ownerID == null) "owner/{ownerID}?edit={editable}"
    else "owner/{$ownerID}?edit={$editable}"
}
val PROJECTS = "projects"
val NEW_PROJECT = {clientID: UUID? ->
    if(clientID == null) "newproject?client={clientID}" else "newproject?client={$clientID}"
}

val bottomItemList = listOf(
    Screens.Home,
    Screens.Company,
    Screens.Clients,
    Screens.Projects
)

sealed class Screens(val title: String, val route: String, @DrawableRes val icons: Int){
    object Home: Screens("home", HOME, R.drawable.ic_outline_calendar_today_24)
    object Company: Screens("company", COMPANY(null), R.drawable.warehouse_black_24dp)
    object Clients: Screens("clients", CLIENTS, R.drawable.ic_outline_people_24)
    object Projects: Screens("projects", PROJECTS, R.drawable.ic_outline_assignment_24)
}

@Composable
fun BottomNavigationBar(pNavController: NavController? = rememberNavController()) {
    var navController = pNavController
    if(navController == null)
        navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navItems = listOf(
        Screens.Home,
        Screens.Projects,
        Screens.Clients,
        Screens.Company
    )
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation(
        backgroundColor = Purple,
        contentColor = White
    ) {
        navItems.forEach { screens ->
            BottomNavigationItem(
                selected = currentDestination?.route == screens.route,
                onClick = {
                    navController.navigate(screens.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screens.icons),
                        contentDescription = null
                    )
                },
                label = { Text(text = screens.title) }
            )
        }
    }
}

@Composable
fun NavigationHost(navController: NavHostController){
    NavHost(navController = navController, startDestination = SIGN_UP) {
        composable(SIGN_IN) { SignIn(navController = navController) }
        composable(SIGN_UP) { SignUp(navController = navController) }
        composable(JOIN_COMPANY) { JoinCompany(navController = navController) }
        composable(HOME) { Home(navController = navController, stateHolder.getProjects(), stateHolder.getMaintenances()) }
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