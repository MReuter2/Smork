package de.mreuter.smork.ui.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.composable
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.database.MainViewModel
import de.mreuter.smork.ui.elements.BottomNavigationBar
import de.mreuter.smork.ui.screens.project.NewProject
import de.mreuter.smork.ui.screens.project.Project
import de.mreuter.smork.ui.screens.project.ProjectEditView
import de.mreuter.smork.ui.screens.project.Projects


fun NavGraphBuilder.projectGraph(
    navController: NavController,
    viewModel: MainViewModel,
) {
    navigation(
        startDestination = Screen.Projects.route + "/projects",
        route = Screen.Projects.route
    ) {
        composable(Screen.Projects.route + "/projects") {
            val projects = viewModel.projectService.findAllProjects()
                Projects(
                    projects = projects,
                    navigateToNewProject = { navController.navigate(Screen.Projects.route + "/newProject") },
                    navigateToProject = { project ->
                        navController.navigate(
                            Screen.Projects.withArgs(
                                project.id.toString()
                            )
                        )
                    }
                ) { BottomNavigationBar(navController = navController) }
        }
        composable(Screen.Projects.route + "/newProject?clientID={clientID}") {
            var preselectedClient: Client? = null
            val allClients = viewModel.clientService.findAllClients()
            if (it.arguments?.getString("clientID") != null && it.arguments?.getString("clientID") != null ) {
                val clientId = it.arguments?.getString("clientID") ?: throw RuntimeException("Wrong")
                preselectedClient = viewModel.clientService.findClientById(clientId)
            }
            if(allClients.isNotEmpty()){
                NewProject(
                    preselectedClient = preselectedClient,
                    clients = allClients,
                    onProjectSave = { newProject ->
                        viewModel.projectService.insertProject(newProject)
                        navController.navigate(Screen.Projects.withArgs(newProject.id.toString()))
                    },
                    bottomBar = { BottomNavigationBar(navController = navController) },
                    backNavigation = { navController.popBackStack() }
                )
            }
        }
        composable(Screen.Projects.route + "/{projectID}?edit={edit}",
            arguments = listOf(
                navArgument("edit"){
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            val projectId = it.arguments?.getString("projectID") ?: throw RuntimeException("This is not an ID")
            val project = viewModel.projectService.findProjectById(projectId)
            val edit = remember { mutableStateOf(it.arguments?.getBoolean("edit") ?: false) }
            val allClients = viewModel.clientService.findAllClients()

            if(project != null) {
                if (!edit.value) {
                    Project(
                        project = project,
                        bottomBar = { BottomNavigationBar(navController = navController) },
                        backNavigation = { navController.navigate(Screen.Projects.route) },
                        navigateToEditView = { edit.value = true },
                        onProjectUpdate = { updatedProject ->
                            viewModel.projectService.insertProject(updatedProject)
                            navController.navigate(Screen.Projects.withArgs(updatedProject.id.toString()))
                        }
                    )
                } else {
                    if(allClients.isNotEmpty()){
                        ProjectEditView(
                            project = project,
                            clients = allClients,
                            bottomBar = { BottomNavigationBar(navController) },
                            backNavigation = { edit.value = false },
                            onProjectDelete = { oldProject ->
                                navController.popBackStack()
                                viewModel.projectService.deleteProject(oldProject)
                            },
                            onProjectUpdate = { updatedProject ->
                                viewModel.projectService.insertProject(updatedProject)
                                edit.value = false
                            }
                        )
                    }
                }
            }
        }
    }
}