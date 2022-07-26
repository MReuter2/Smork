package de.mreuter.smork.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.*
import androidx.navigation.compose.composable
import de.mreuter.smork.backend.database.MainViewModel
import de.mreuter.smork.ui.elements.BottomNavigationBar
import de.mreuter.smork.ui.screens.ClientEditingView
import de.mreuter.smork.ui.screens.ClientView
import de.mreuter.smork.ui.screens.Clients


fun NavGraphBuilder.clientGraph(navController: NavController, viewModel: MainViewModel) {
    navigation(startDestination = Screen.Clients.route + "/clients", route = Screen.Clients.route) {
        composable(Screen.Clients.route + "/clients") {
            val allClients = viewModel.findAllClients()
            Clients(
                navigateToClient = { navController.navigate(Screen.Clients.withArgs(it.id.toString())) },
                navigateToNewClient = { navController.navigate(Screen.Clients.route + "/newClient") },
                bottomBar = { BottomNavigationBar(navController) },
                clients = allClients
            )
        }
        composable(Screen.Clients.route + "/newClient") {
            ClientEditingView(
                onClientSave = { newClient ->
                    viewModel.insertClient(newClient)
                    navController.navigate(Screen.Clients.withArgs(newClient.id.toString()))
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
                val clientId = it.arguments?.getString("clientID")
                    ?: throw RuntimeException("No Client with ID: " + it.arguments?.getString("clientID"))
                val client = viewModel.findClientById(clientId)
                if (client != null) {
                    if (it.arguments?.getBoolean("edit") == false) {
                        ClientView(
                            client = client,
                            navigateToEditView = {
                                navController.navigate(
                                    Screen.Clients.withArgs(
                                        clientId,
                                        "?edit=true"
                                    )
                                )
                            },
                            navigateToNewProject = { preselectedClient ->
                                navController.navigate(
                                    Screen.Projects.route + "/newProject?clientID=${preselectedClient.id}"
                                )
                            },
                            navigateToProject = { project ->
                                navController.navigate(
                                    Screen.Projects.withArgs(
                                        project.id.toString()
                                    )
                                )
                            },
                            bottomBar = { BottomNavigationBar(navController) }
                        ) { navController.navigate(Screen.Clients.route) }
                    } else {
                        ClientEditingView(
                            client = client,
                            onClientSave = { newClient ->
                                viewModel.insertClient(newClient)
                                navController.navigate(Screen.Clients.withArgs(newClient.id.toString()))
                            },
                            bottomBar = { BottomNavigationBar(navController) },
                            backNavigation = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}