package de.mreuter.smork.ui.navigation

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.mreuter.smork.R
import de.mreuter.smork.*
import de.mreuter.smork.backend.Client
import de.mreuter.smork.backend.EmailAddress
import de.mreuter.smork.backend.Fullname
import de.mreuter.smork.ui.elements.BasicScaffold
import de.mreuter.smork.ui.elements.BottomNavigationBar
import de.mreuter.smork.ui.elements.TopBar
import de.mreuter.smork.ui.screens.*
import java.util.*

sealed class Screen(
    val route: String,
    @DrawableRes val unselectedIcon: Int? = null,
    @DrawableRes val selectedIcon: Int? = null,
    val title: String = ""
) {
    object Login : Screen("login")
    object SignIn : Screen("signIn")
    object SignUp : Screen("signUp")
    object JoinCompany : Screen("joinCompany")

    object Home : Screen("home", R.drawable.ic_outline_calendar_today_24, R.drawable.ic_baseline_calendar_today_24, "Home")

    object Company :
        Screen("company", R.drawable.ic_outlined_warehouse, R.drawable.ic_baseline_warehouse, "Company")

    object Clients :
        Screen("client_view", R.drawable.ic_outline_people_24, R.drawable.ic_baseline_people_24, "Clients")

    object Projects : Screen("project_view", R.drawable.ic_outline_assignment_24, R.drawable.ic_baseline_assignment_24, "Projects")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                if (it[0] == '?') {
                    append(it)
                } else {
                    append("/$it")
                }
            }
        }
    }
}

fun NavGraphBuilder.loginGraph(navController: NavController) {
    navigation(startDestination = Screen.SignIn.route, route = Screen.Login.route) {
        composable(Screen.SignIn.route) {
            SignIn(
                navigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                navigateToHome = { navController.navigate(Screen.Home.route) }
            )
        }
        composable(Screen.SignUp.route) {
            SignUp(
                navigateToSignIn = { navController.navigate(Screen.SignIn.route) },
                navigateToJoinCompany = { fullname, emailAddress, phoneNumber ->
                    navController.navigate(
                        Screen.JoinCompany.withArgs(
                            fullname.firstname,
                            fullname.lastname,
                            emailAddress.emailAddress,
                            phoneNumber.toString()
                        )
                    )
                }
            )
        }
        composable(Screen.JoinCompany.route + "/{firstname}/{lastname}/{emailaddress}/{phonenumber}",
            arguments = listOf(
                navArgument("phonenumber") {
                    type = NavType.LongType
                }
            )
        ) {
            val fullname = Fullname(
                it.arguments?.getString("firstname") ?: throw RuntimeException(),
                it.arguments?.getString("lastname") ?: throw RuntimeException()
            )
            val emailAddress =
                if (it.arguments?.getString("emailaddress") == null) null else EmailAddress(
                    it.arguments?.getString("emailaddress")!!
                )
            val phonenumber = it.arguments?.getLong("phonenumber")
            JoinCompany(fullname, emailAddress, phonenumber)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.clientGraph(navController: NavController) {
    navigation(startDestination = Screen.Clients.route + "/clients", route = Screen.Clients.route) {
        composable(Screen.Clients.route + "/clients") {
            Clients(
                navigateToClient = { navController.navigate(Screen.Clients.withArgs(it.uuid.toString())) },
                navigateToNewClient = { navController.navigate(Screen.Clients.route + "/newClient") },
                bottomBar = { BottomNavigationBar(navController) },
            )
        }
        composable(Screen.Clients.route + "/newClient") {
            ClientCreatingView(
                navigateToClient = {
                    navController.navigate(
                        Screen.Clients.withArgs(
                            it.uuid.toString()
                        )
                    )
                },
                bottomBar = { BottomNavigationBar(navController) },
                backNavigation = { navController.popBackStack() }
            )
        }
        composable(Screen.Clients.route + "/{clientID}?edit={edit}",
            arguments = listOf(
                navArgument("edit") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            if (it.arguments?.getString("clientID") != null) {
                val client =
                    stateHolder.getClientByID(UUID.fromString(it.arguments?.getString("clientID")))
                        ?: throw RuntimeException("No Client with ID: " + it.arguments?.getString("clientID"))
                if (it.arguments?.getBoolean("edit") == false) {
                    ClientView(
                        client = client,
                        changeToEditView = {
                            navController.navigate(
                                Screen.Clients.withArgs(
                                    client.uuid.toString(),
                                    "?edit=true"
                                )
                            )
                        },
                        navigateToNewProject = { preselectedClient -> navController.navigate(Screen.Projects.route + "/newProject?clientID=${preselectedClient.uuid}") },
                        navigateToProject = { project ->
                            navController.navigate(
                                Screen.Projects.withArgs(
                                    project.uuid.toString()
                                )
                            )
                        },
                        bottomBar = { BottomNavigationBar(navController) },
                        backNavigation = { navController.navigate(Screen.Clients.route) }
                    )
                } else {
                    ClientEditingView(
                        client = client,
                        navigateToClient = { editedClient ->
                            navController.navigate(
                                Screen.Clients.withArgs(editedClient.uuid.toString())
                            )
                        },
                        bottomBar = { BottomNavigationBar(navController) },
                        backNavigation = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.projectGraph(navController: NavController) {
    navigation(
        startDestination = Screen.Projects.route + "/projects",
        route = Screen.Projects.route
    ) {
        composable(Screen.Projects.route + "/projects") {
            Projects(
                projects = stateHolder.getProjects(),
                navigateToNewProject = { navController.navigate(Screen.Projects.route + "/newProject") },
                navigateToProject = { project ->
                    navController.navigate(
                        Screen.Projects.withArgs(
                            project.uuid.toString()
                        )
                    )
                },
                bottomBar = { BottomNavigationBar(navController = navController) }
            )
        }
        composable(Screen.Projects.route + "/newProject?clientID={clientID}",
            arguments = listOf(
                navArgument("clientID") {
                    defaultValue = null
                    nullable = true
                }
            )
        ) {
            var preselectedClient: Client? = null
            if (it.arguments?.getString("clientID") != null)
                preselectedClient =
                    stateHolder.getClientByID(UUID.fromString(it.arguments?.getString("clientID")))
            NewProject(
                preselectedClient = preselectedClient,
                clients = stateHolder.getClients(),
                navigateToClient = { client ->
                    navController.navigate(
                        Screen.Clients.withArgs(client.uuid.toString())
                    )
                },
                bottomBar = { BottomNavigationBar(navController = navController) },
                backNavigation = { navController.popBackStack() }
            )
        }
        composable(Screen.Projects.route + "/{projectID}") {
            val project =
                stateHolder.getProjectByID(UUID.fromString(it.arguments?.getString("projectID")))
            Project(
                project = project,
                bottomBar = { BottomNavigationBar(navController = navController) },
                backNavigation = { navController.popBackStack() })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        loginGraph(navController)
        clientGraph(navController)
        projectGraph(navController)
        composable(Screen.Home.route) {
            Home(
                stateHolder.getProjects(),
                stateHolder.getMaintenances(),
                navigateToProject = { project ->
                    navController.navigate(
                        Screen.Projects.withArgs(
                            project.uuid.toString()
                        )
                    )
                },
                bottomBar = { BottomNavigationBar(navController) }
            )
        }
        composable(Screen.Company.route) {
            YourCompany(
                company = stateHolder.usersCompany() ?: throw RuntimeException("No Company"),
                bottomBar = { BottomNavigationBar(navController) }
            )
        }
    }
}