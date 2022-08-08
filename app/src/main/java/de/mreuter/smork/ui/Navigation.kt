package de.mreuter.smork.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import de.mreuter.smork.R
import de.mreuter.smork.backend.company.application.fromCompany
import de.mreuter.smork.backend.database.MainViewModel
import de.mreuter.smork.ui.client.navigation.clientGraph
import de.mreuter.smork.ui.company.navigation.companyGraph
import de.mreuter.smork.ui.project.navigation.projectGraph
import de.mreuter.smork.ui.screens.home.CreateCompany
import de.mreuter.smork.ui.utils.BottomNavigationBar

sealed class Screen(
    val route: String,
    @DrawableRes val unselectedIcon: Int? = null,
    @DrawableRes val selectedIcon: Int? = null,
    val title: String = ""
) {
    object Start : Screen("start")

    object Company :
        Screen("company_view", R.drawable.ic_outlined_warehouse, R.drawable.ic_baseline_warehouse, "Company")

    object Clients :
        Screen("client_view", R.drawable.ic_outline_people_24, R.drawable.ic_baseline_people_24, "Clients")

    object Projects :
        Screen("project_view", R.drawable.ic_outline_assignment_24, R.drawable.ic_baseline_assignment_24, "Projects")

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

fun NavGraphBuilder.startGraph(navController: NavController, viewModel: MainViewModel) {
    navigation(startDestination = Screen.Start.route + "/createCompany", route = Screen.Start.route) {
        composable(Screen.Start.route + "/createCompany") {
            CreateCompany(onCompanySave = { company ->
                viewModel.companyService.insertCompany(fromCompany(company))
                navController.navigate(Screen.Company.route)
            })
        }
    }
}

@Composable
fun NavigationHost(viewModel: MainViewModel){
    val navController = rememberNavController()
    val company = viewModel.companyService.findCompany()
    val startDestination = if(company != null) Screen.Company.route else Screen.Start.route
    NavHost(navController = navController, startDestination = startDestination) {
        startGraph(navController, viewModel)
        clientGraph(navController, viewModel)
        projectGraph(navController, viewModel)
        companyGraph(navController, viewModel)
    }
}