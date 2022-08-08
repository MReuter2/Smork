package de.mreuter.smork.backend.database

import android.app.Application
import androidx.lifecycle.ViewModel
import de.mreuter.smork.backend.client.application.ClientService
import de.mreuter.smork.backend.client.domain.ClientRepository
import de.mreuter.smork.backend.company.application.CompanyService
import de.mreuter.smork.backend.company.domain.CompanyRepository
import de.mreuter.smork.backend.owner.application.OwnerService
import de.mreuter.smork.backend.owner.domain.OwnerRepository
import de.mreuter.smork.backend.project.application.ProjectService
import de.mreuter.smork.backend.project.domain.ProjectRepository
import de.mreuter.smork.backend.project.domain.TaskRepository
import de.mreuter.smork.backend.worker.application.WorkerService
import de.mreuter.smork.backend.worker.domain.WorkerRepository

class MainViewModel(application: Application): ViewModel() {
    private val projectRepository: ProjectRepository
    private val companyRepository: CompanyRepository
    private val ownerRepository: OwnerRepository
    private val workerRepository: WorkerRepository
    private val clientRepository: ClientRepository
    private val taskRepository: TaskRepository

    init{
        val database = SmorkDatabase.getInstance(application)
        val projectDao = database.projectDao()
        val companyDao = database.companyDao()
        val ownerDao = database.ownerDao()
        val workerDao = database.workerDao()
        val clientDao = database.clientDao()
        val taskDao = database.taskDao()
        projectRepository = ProjectRepository(projectDao)
        companyRepository = CompanyRepository(companyDao)
        ownerRepository = OwnerRepository(ownerDao)
        workerRepository = WorkerRepository(workerDao)
        clientRepository = ClientRepository(clientDao)
        taskRepository = TaskRepository(taskDao)
    }

    val companyService = CompanyService(companyRepository)
    val ownerService = OwnerService(ownerRepository)
    val workerService = WorkerService(workerRepository)
    val clientService = ClientService(clientRepository)
    val projectService = ProjectService(projectRepository, taskRepository, clientService)
}