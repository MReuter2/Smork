package de.mreuter.smork.ui.company.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.composable
import de.mreuter.smork.backend.company.application.fromCompany
import de.mreuter.smork.backend.database.MainViewModel
import de.mreuter.smork.ui.Screen
import de.mreuter.smork.ui.company.views.*
import de.mreuter.smork.ui.utils.BottomNavigationBar

fun NavGraphBuilder.companyGraph(navController: NavController, viewModel: MainViewModel) {
    navigation(startDestination = Screen.Company.route + "/company?edit={edit}", route = Screen.Company.route) {
        composable(
            Screen.Company.route + "/company?edit={edit}",
                    arguments = listOf(
                        navArgument("edit"){
                            type = NavType.BoolType
                            defaultValue = false
                        }
                    )
        ){
            val company = viewModel.companyService.findCompany()
            val owner = viewModel.ownerService.findAllOwner()
            val worker = viewModel.workerService.findAllWorker()

            val edit = remember{ mutableStateOf(it.arguments?.getBoolean("edit") ?: false) }

            if(company != null){
                if (!edit.value) {
                    YourCompany(
                        company = company,
                        owner = owner,
                        worker = worker,
                        onCompanyEdit = { edit.value = true },
                        onWorkerClick = { navController.navigate(Screen.Company.withArgs(it.id.toString())) },
                        onOwnerClick = { navController.navigate(Screen.Company.withArgs(it.id.toString())) },
                        onClickOwnerAddButton = { navController.navigate(Screen.Company.route + "/newOwner") },
                        onClickWorkerAddButton = { navController.navigate(Screen.Company.route + "/newWorker") }
                    ) {
                        BottomNavigationBar(navController = navController)
                    }
                } else {
                    CompanyEditing(
                        company = company,
                        onCompanySave = { updatedCompany ->
                            viewModel.companyService.insertCompany(fromCompany(updatedCompany))
                            edit.value = false
                        },
                        backNavigation = { edit.value = false }
                    ) {
                        BottomNavigationBar(navController = navController)
                    }
                }
            }
        }
        composable(Screen.Company.route + "/newOwner") {
            OwnerCreating(
               onOwnerSave = {
                   viewModel.ownerService.insertOwner(it)
                   navController.popBackStack() },
               backNavigation = { navController.popBackStack() }
            ){ BottomNavigationBar(navController) }
        }
        composable(Screen.Company.route + "/newWorker") {
            WorkerCreating(
                onWorkerSave = {
                    viewModel.workerService.insertWorker(it)
                    navController.popBackStack() },
                backNavigation = { navController.popBackStack() }
            ){ BottomNavigationBar(navController) }
        }
        composable(
            Screen.Company.route + "/{personId}?edit={edit}",
            arguments = listOf(
                navArgument("edit") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            if (it.arguments?.getString("personId") != null) {
                val personId = it.arguments?.getString("personId")
                    ?: throw RuntimeException("No Person with ID: " + it.arguments?.getString("personId"))
                val edit = remember{ mutableStateOf(it.arguments?.getBoolean("edit")?: false) }
                val owner = viewModel.ownerService.findOwnerById(personId)
                val worker = viewModel.workerService.findWorkerById(personId)
                if(owner != null){
                    if(edit.value){
                        OwnerEditing(
                            owner = owner,
                            onOwnerSave = { updatedOwner ->
                                viewModel.ownerService.insertOwner(updatedOwner)
                                edit.value = false },
                            onOwnerDelete = { deleteOwner ->
                                navController.popBackStack()
                                viewModel.ownerService.deleteOwner(deleteOwner)
                            },
                            backNavigation = { edit.value = false }
                        ) { BottomNavigationBar(navController) }
                    }else {
                        OwnerView(
                            owner = owner,
                            navigateToEditView = { edit.value = true },
                            backNavigation = { navController.popBackStack() }
                        ) { BottomNavigationBar(navController) }
                    }
                }else
                    if(worker != null){
                        if(edit.value){
                            WorkerEditing(
                                worker = worker,
                                onWorkerSave = { updatedWorker ->
                                    viewModel.workerService.insertWorker(updatedWorker)
                                    edit.value = false },
                                onWorkerDelete = { deleteWorker ->
                                    navController.popBackStack()
                                    viewModel.workerService.deleteWorker(deleteWorker)
                                },
                                backNavigation = { edit.value = false }
                            ) { BottomNavigationBar(navController) }
                        }else{
                            WorkerView(
                                worker = worker,
                                navigateToEditView = { edit.value = true },
                                backNavigation = { navController.popBackStack() }
                            ) { BottomNavigationBar(navController) }
                        }
                    }
            }
        }
    }
}