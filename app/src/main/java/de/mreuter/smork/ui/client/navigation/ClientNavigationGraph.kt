package de.mreuter.smork.ui.client.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.composable
import de.mreuter.smork.backend.database.MainViewModel
import de.mreuter.smork.ui.Screen
import de.mreuter.smork.ui.client.views.ClientCreating
import de.mreuter.smork.ui.client.views.ClientEditingView
import de.mreuter.smork.ui.client.views.ClientView
import de.mreuter.smork.ui.client.views.Clients
import de.mreuter.smork.ui.utils.BottomNavigationBar


fun NavGraphBuilder.clientGraph(navController: NavController, viewModel: MainViewModel) {
    navigation(startDestination = Screen.Clients.route + "/clients", route = Screen.Clients.route) {
        composable(Screen.Clients.route + "/clients") {
            val allClients = viewModel.clientService.findAllClients()
            Clients(
                navigateToClient = { navController.navigate(Screen.Clients.withArgs(it.id.toString())) },
                navigateToNewClient = { navController.navigate(Screen.Clients.route + "/newClient") },
                bottomBar = { BottomNavigationBar(navController) },
                clients = allClients
            )
        }
        composable(Screen.Clients.route + "/newClient") {
            ClientCreating(
                onClientSave = { newClient ->
                    viewModel.clientService.insertClient(newClient)
                    navController.navigate(Screen.Clients.withArgs(newClient.id.toString()))
                },
                bottomBar = { BottomNavigationBar(navController) },
                backNavigation = { navController.popBackStack() }
            )
        }
        composable(
            Screen.Clients.route + "/{clientID}?edit={edit}",
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
                val edit = remember{ mutableStateOf(it.arguments?.getBoolean("edit") ?: false) }

                val client = viewModel.clientService.findClientById(clientId)
                val projects = viewModel.projectService.findProjectsByClientId(clientId)
                if (client != null) {
                    if (!edit.value) {
                        ClientView(
                            client = client,
                            projects = projects,
                            navigateToEditView = {edit.value = true},
                            navigateToNewProject = { preselectedClient ->
                                navController.navigate(
                                    Screen.Projects.route + "/newProject?clientID=${preselectedClient.id}"
                                )
                            },
                            navigateToProject = { project ->
                                navController.navigate(
                                    Screen.Projects.withArgs(project.id.toString())
                                )
                            },
                            bottomBar = { BottomNavigationBar(navController) }
                        ) { navController.navigate(Screen.Clients.route) }
                    } else {
                        ClientEditingView(
                            client = client,
                            onClientSave = { newClient ->
                                viewModel.clientService.insertClient(newClient)
                                edit.value = false
                            },
                            onClientDelete = { deleteClient ->
                                viewModel.clientService.deleteClient(deleteClient)
                                navController.popBackStack()
                            },
                            bottomBar = { BottomNavigationBar(navController) },
                            backNavigation = { edit.value = false }
                        )
                    }
                }
            }
        }
    }
}