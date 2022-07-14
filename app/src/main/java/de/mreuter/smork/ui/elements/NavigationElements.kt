package de.mreuter.smork.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.mreuter.smork.R
import de.mreuter.smork.ui.navigation.Screen
import de.mreuter.smork.ui.theme.FreelancerTheme

@Composable
fun TopBar(
    title: String,
    trailingIcons: @Composable () -> Unit = {},
    showNavigationIcon: Boolean = false,
    navigationToPreviousPage: () -> Unit = {}
) {
    if(showNavigationIcon){
        TopAppBar(
            navigationIcon = {
                IconButton(
                    onClick = { navigationToPreviousPage() }
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            },
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colors.onPrimary
                    )
                    Row{
                        trailingIcons()
                    }
                }
            },
            backgroundColor = MaterialTheme.colors.primary
        )
    }else{
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colors.onPrimary
                    )
                    trailingIcons()
                }
            },
            backgroundColor = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController = rememberNavController()) {
    val navItems = listOf(
        Screen.Home,
        Screen.Projects,
        Screen.Clients,
        Screen.Company
    )
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        navItems.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    if(screen.unselectedIcon != null && screen.selectedIcon != null) {
                        if(isSelected) {
                            Icon(
                                painter = painterResource(id = screen.selectedIcon),
                                contentDescription = null
                            )
                        }else{
                            Icon(
                                painter = painterResource(id = screen.unselectedIcon),
                                contentDescription = null
                            )
                        }
                    }
                },
                label = { Text(text = screen.title, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Light)}
            )
        }
    }
}

@Composable
fun BasicScaffold(
    bottomBar: @Composable () -> Unit,
    topBarTitle: String,
    backNavigation: (() -> Unit)? = null,
    trailingAppBarIcons: @Composable () -> Unit = {},
    content: @Composable () -> Unit
){
    Scaffold(
        topBar = {
            TopBar(
                title = topBarTitle,
                showNavigationIcon = backNavigation != null,
                navigationToPreviousPage = backNavigation ?: {},
                trailingIcons = trailingAppBarIcons
            )
        },
        bottomBar = {
            bottomBar()
        }
    ) {
        content()
    }
}

@Preview
@Composable
fun PreviewTopBar() {
    FreelancerTheme {
        TopBar(stringResource(R.string.app_name))
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    FreelancerTheme {
        BottomNavigationBar(rememberNavController())
    }
}