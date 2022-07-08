package de.mreuter.smork.ui.navigation

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.mreuter.smork.R
import de.mreuter.smork.*
import de.mreuter.smork.backend.EmailAddress
import de.mreuter.smork.backend.Fullname
import de.mreuter.smork.backend.exampleCompanies
import de.mreuter.smork.ui.elements.BasicScaffold
import de.mreuter.smork.ui.elements.BottomNavigationBar
import de.mreuter.smork.ui.elements.TopBar
import de.mreuter.smork.ui.screens.*
import de.mreuter.smork.ui.theme.White
import java.util.*

sealed class Screen(val route: String, @DrawableRes val icons: Int? = null) {
    object Login : Screen("login")
    object SignIn : Screen("signIn")
    object SignUp : Screen("signUp")
    object JoinCompany : Screen("joinCompany")

    object Home : Screen("home", R.drawable.ic_outline_calendar_today_24)

    object Company : Screen("company", R.drawable.warehouse_black_24dp)

    object Clients : Screen("client_view", R.drawable.ic_outline_people_24)

    object Projects : Screen("project_view", R.drawable.ic_outline_assignment_24)

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

fun NavGraphBuilder.clientGraph(navController: NavController) {
    navigation(startDestination = Screen.Clients.route + "/clients", route = Screen.Clients.route) {
        composable(Screen.Clients.route + "/clients") {
            Scaffold(
                topBar = {
                    TopBar {
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.Clients.route + "/newClient")
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_add_24),
                                contentDescription = null,
                                tint = White
                            )
                        }
                    }
                },
                bottomBar = { BottomNavigationBar(navController) }
            ) {
                Clients(navigateToClient = { navController.navigate(Screen.Clients.withArgs(it.uuid.toString())) })
            }
        }
        composable(Screen.Clients.route + "/newClient") {
            BasicScaffold(navController = navController) {
                ClientCreatingView(navigateToClient = {
                    navController.navigate(
                        Screen.Clients.withArgs(
                            it.uuid.toString()
                        )
                    )
                })
            }
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
                    BasicScaffold(navController = navController) {
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
                            navigateToNewProject = { navController.navigate(Screen.Projects.route + "/newProject") }
                        )
                    }
                } else {
                    BasicScaffold(navController = navController) {
                        ClientEditingView(
                            client,
                            navigateToClient = { client ->
                                navController.navigate(
                                    Screen.Clients.withArgs(client.uuid.toString())
                                )
                            })
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.projectGraph(navController: NavController) {
    navigation(startDestination = Screen.Projects.route + "/projects", route = Screen.Projects.route) {
        composable(Screen.Projects.route + "/projects") {
            BasicScaffold(navController = navController) {
                Projects(
                    stateHolder.getProjects(),
                    navigateToNewProject = { navController.navigate(Screen.Projects.route + "/newProject") },
                    navigateToProject = { project ->
                        navController.navigate(
                            Screen.Projects.withArgs(
                                project.uuid.toString()
                            )
                        )
                    }
                )
            }
        }
        composable(Screen.Projects.route + "/newProject") {
            BasicScaffold(navController = navController) {
                NewProject(
                    clients = stateHolder.getClients(),
                    navigateToProject = { project ->
                        navController.navigate(
                            Screen.Projects.withArgs(project.uuid.toString())
                        )
                    }
                )
            }
        }
        composable(Screen.Projects.route + "/{projectID}") {
            val project =
                stateHolder.getProjectByID(UUID.fromString(it.arguments?.getString("projectID")))
            BasicScaffold(navController = navController) { Project(project) }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        loginGraph(navController)
        clientGraph(navController)
        projectGraph(navController)
        composable(Screen.Home.route) {
            BasicScaffold(navController = navController) {
                Home(
                    stateHolder.getProjects(),
                    stateHolder.getMaintenances(),
                    navigateToProject = { project ->
                        navController.navigate(
                            Screen.Projects.withArgs(
                                project.uuid.toString()
                            )
                        )
                    }
                )
            }
        }
        composable(Screen.Company.route) {
            BasicScaffold(navController = navController) {
                YourCompany(
                    stateHolder.usersCompany() ?: throw RuntimeException("No Company")
                )
            }
        }
    }
}