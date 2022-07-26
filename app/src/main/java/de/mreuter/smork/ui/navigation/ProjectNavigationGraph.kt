package de.mreuter.smork.ui.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.database.MainViewModel
import de.mreuter.smork.backend.project.application.toProject
import de.mreuter.smork.ui.elements.BottomNavigationBar
import de.mreuter.smork.ui.screens.NewProject
import de.mreuter.smork.ui.screens.Project
import de.mreuter.smork.ui.screens.ProjectEditView
import de.mreuter.smork.ui.screens.Projects


fun NavGraphBuilder.projectGraph(
    navController: NavController,
    viewModel: MainViewModel,
) {
    navigation(
        startDestination = Screen.Projects.route + "/projects",
        route = Screen.Projects.route
    ) {
        composable(Screen.Projects.route + "/projects") {
            val projects = viewModel.findAllProjects()
            if(projects.isNotEmpty()){
                Projects(
                    projects = projects, //TODO: findAllProjects()
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
        }
        composable(Screen.Projects.route + "/newProject?clientID={clientID}") {
            var preselectedClient: Client? = null
            val allClients = viewModel.findAllClients()
            if (it.arguments?.getString("clientID") != null && it.arguments?.getString("clientID") != null ) {
                val clientId = it.arguments?.getString("clientID") ?: throw RuntimeException("Wrong")
                preselectedClient = viewModel.findClientById(clientId)
            }
            if(allClients.isNotEmpty()){
                NewProject(
                    preselectedClient = preselectedClient,
                    clients = allClients,
                    onProjectSave = { newProject ->
                        viewModel.insertProject(newProject)
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
            val project = viewModel.findProjectById(projectId)
            //val projectSearchResult by viewModel.projectSearchResults.observeAsState()
            val edit = it.arguments?.getBoolean("edit") ?: false
            //val project = projectSearchResult?.first()
            val allClients = viewModel.findAllClients()


            if(project != null) {
                if (!edit) {
                    Project(
                        project = project,
                        bottomBar = { BottomNavigationBar(navController = navController) },
                        backNavigation = { navController.navigate(Screen.Projects.route) },
                        navigateToEditView = { navController.navigate(Screen.Projects.withArgs("$projectId?edit=true")) },
                        onProjectUpdate = { updatedProject ->
                            viewModel.insertProject(updatedProject)
                            navController.navigate(Screen.Projects.withArgs(updatedProject.id.toString()))
                        }
                    )
                } else {
                    if(allClients.isNotEmpty()){
                        ProjectEditView(
                            project = project,
                            clients = allClients,
                            bottomBar = { BottomNavigationBar(navController) },
                            navigateToProjects = { navController.navigate(Screen.Projects.route + "/projects") },
                            onProjectDelete = { oldProject ->
                                navController.navigate(Screen.Projects.route + "/projects")
                                viewModel.deleteProject(oldProject)
                            },
                            onProjectUpdate = { updatedProject ->
                                viewModel.insertProject(updatedProject)
                                navController.navigate(Screen.Projects.withArgs(updatedProject.id.toString()))
                            }
                        )
                    }
                }
            }
        }
    }
}