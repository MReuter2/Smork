package de.mreuter.smork.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.mreuter.smork.R
import de.mreuter.smork.backend.database.MainViewModel
import de.mreuter.smork.ui.elements.BottomNavigationBar
import de.mreuter.smork.ui.screens.*

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

fun NavGraphBuilder.loginGraph(navController: NavController, viewModel: MainViewModel) {
    navigation(startDestination = Screen.JoinCompany.route, route = Screen.Login.route) {
        /*composable(Screen.SignIn.route) {
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
        }*/
        composable(Screen.JoinCompany.route) {
            JoinCompany(onCompanySave = {company ->
                viewModel.insertCompany(company)
                navController.navigate(Screen.Home.route)
            })
        }
    }
}

@Composable
fun NavigationHost(
    viewModel: MainViewModel
){
    val navController = rememberNavController()
    val company = viewModel.findCompany()
    val startDestination = if(company != null) Screen.Home.route else Screen.Login.route
    NavHost(navController = navController, startDestination = startDestination) {
        loginGraph(navController, viewModel)
        clientGraph(navController, viewModel)
        projectGraph(navController, viewModel)
        composable(Screen.Home.route) {
            val allProjects = viewModel.findAllProjects()
            Home(
                projects = allProjects, //TODO: FindAllActiveProjects()
                listOf(), /*TODO: Persist maintenances*/
                navigateToProject = { project ->
                    navController.navigate(
                        Screen.Projects.withArgs(
                            project.id.toString()
                        )
                    )
                },
                bottomBar = { BottomNavigationBar(navController) }
            )
        }
        composable(Screen.Company.route) {
            val allOwner = viewModel.findAllOwner()
            val allWorker = viewModel.findAllWorker()
            YourCompany(
                company = company ?: throw RuntimeException(),
                bottomBar = { BottomNavigationBar(navController) },
                owner = allOwner,
                worker = allWorker //TODO findOwnerByCompanyID()
            )
        }
    }
}