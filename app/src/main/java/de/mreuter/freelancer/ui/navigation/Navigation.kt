package de.mreuter.freelancer.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import de.mreuter.freelancer.R
import de.mreuter.freelancer.ui.screens.YourCompany
import de.mreuter.freelancer.ui.screens.Clients
import de.mreuter.freelancer.ui.screens.Home
import de.mreuter.freelancer.ui.screens.Projects

sealed class Screens(val title: String, val route: String, @DrawableRes val icons: Int){
    object Home: Screens("home", "home_route", R.drawable.ic_outline_calendar_today_24)
    object Company: Screens("company", "company_route", R.drawable.warehouse_black_24dp)
    object Clients: Screens("clients", "clients_route", R.drawable.ic_outline_people_24)
    object Projects: Screens("projects", "projects_route", R.drawable.ic_outline_assignment_24)
}

@Composable
fun BottomNavHost(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = Screens.Home.route){
        composable(Screens.Home.route){
            Home(navController = navHostController)
        }
        composable(Screens.Company.route){
            YourCompany(navController = navHostController)
        }
        composable(Screens.Clients.route){
            Clients(navController = navHostController)
        }
        composable(Screens.Projects.route){
            Projects(navController = navHostController)
        }
    }
}

@Composable
fun BottomNavigationScreen(navController: NavController, items: List<Screens>){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation {
        items.forEach{ screens ->
            BottomNavigationItem(
                selected = currentDestination?.route == screens.route,
                onClick = {
                    navController.navigate(screens.route)
                },
                icon = { Icon(painter = painterResource(id = screens.icons), contentDescription = null)},
                label = { Text(text = screens.title) }
            )
        }
    }
}