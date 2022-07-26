package de.mreuter.smork.backend.database

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModel
import de.mreuter.smork.backend.client.application.fromClient
import de.mreuter.smork.backend.client.application.toClient
import de.mreuter.smork.backend.client.application.toClients
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.client.domain.ClientRepository
import de.mreuter.smork.backend.company.application.CompanyEntity
import de.mreuter.smork.backend.company.application.toCompany
import de.mreuter.smork.backend.company.domain.Company
import de.mreuter.smork.backend.company.domain.CompanyRepository
import de.mreuter.smork.backend.owner.application.toOwner
import de.mreuter.smork.backend.owner.domain.Owner
import de.mreuter.smork.backend.owner.domain.OwnerRepository
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.backend.project.application.fromProject
import de.mreuter.smork.backend.project.application.toProject
import de.mreuter.smork.backend.project.application.toProjects
import de.mreuter.smork.backend.project.domain.ProjectRepository
import de.mreuter.smork.backend.worker.application.toWorker
import de.mreuter.smork.backend.worker.domain.Worker
import de.mreuter.smork.backend.worker.domain.WorkerRepository

class MainViewModel(application: Application): ViewModel() {

    private val projectRepository: ProjectRepository
    private val companyRepository: CompanyRepository
    private val ownerRepository: OwnerRepository
    private val workerRepository: WorkerRepository
    private val clientRepository: ClientRepository

    init{
        val database = SmorkDatabase.getInstance(application)
        val projectDao = database.projectDao()
        val companyDao = database.companyDao()
        val ownerDao = database.ownerDao()
        val workerDao = database.workerDao()
        val clientDao = database.clientDao()
        projectRepository = ProjectRepository(projectDao)
        companyRepository = CompanyRepository(companyDao)
        ownerRepository = OwnerRepository(ownerDao)
        workerRepository = WorkerRepository(workerDao)
        clientRepository = ClientRepository(clientDao)
    }

    private val company = companyRepository.findCompanyEntity
    private val allOwner = ownerRepository.allOwner
    private val allWorker = workerRepository.allWorker
    private val allProjects = projectRepository.allProjects
    private val allProjectsWithClient = projectRepository.allProjectsWithClient
    private val allClients = clientRepository.allClientEntities

    private val clientSearchResults = clientRepository.searchResults
    private val projectSearchResults = projectRepository.searchResults

    fun insertCompany(companyEntity: CompanyEntity) = companyRepository.insertCompany(companyEntity)

    fun insertClient(newClient: Client){
        clientRepository.insertClient(fromClient(newClient))
    }

    fun insertProject(project: Project){
        val projectEntity = fromProject(project)
        //TODO: Insert Tasks
        projectRepository.insertProject(projectEntity)
    }

    fun deleteProject(project: Project){
        projectRepository.deleteProject(fromProject(project))
    }

    @Composable
    fun findClientById(clientId: String): Client?{
        clientRepository.findClientById(clientId)
        projectRepository.findProjectsByClientId(clientId)
        val clientSearchResult by clientSearchResults.observeAsState(listOf())
        val projects by projectSearchResults.observeAsState(listOf())
        if(clientSearchResult.isNotEmpty() && clientSearchResult.first().id == clientId){
            val result = clientSearchResults.value?.first()
            val client = result?.let { toClient(it) }
            client?.addProjects(toProjects(projects))
            return client
        }
        return null
    }

    @Composable
    fun findAllClients(): List<Client>{
        val allClients by this.allClients.observeAsState(listOf())
        return toClients(allClients)
    }

    @Composable
    fun findAllOwner(): List<Owner>{
        val allOwnerEntities by this.allOwner.observeAsState(listOf())
        val allOwner = mutableListOf<Owner>()
        allOwnerEntities.forEach { allOwner.add(toOwner(it)) }
        return allOwner
    }

    @Composable
    fun findAllWorker(): List<Worker>{
        val allWorkerEntities by this.allWorker.observeAsState(listOf())
        val allWorker = mutableListOf<Worker>()
        allWorkerEntities.forEach { allWorker.add(toWorker(it)) }
        return allWorker
    }

    @Composable
    fun findAllProjects(): List<Project>{
        val allProjectEntities by this.allProjectsWithClient.observeAsState()
        val allProjects = mutableListOf<Project>()
        //TODO: Add Client to the Project
        allProjectEntities?.forEach { (projectEntity, clientEntity) ->
            val project = toProject(projectEntity)
            project.client = toClient(clientEntity)
            allProjects.add(project)
        }
        return allProjects
    }

    @Composable
    fun findProjectById(projectId: String): Project?{
        projectRepository.findProjectById(projectId)
        //TODO: Find Tasks by Project ID
        val projectSearchResult = projectSearchResults.observeAsState(listOf())
        if(projectSearchResult.value.isNotEmpty() && projectSearchResult.value.first().id == projectId){
            val client = findClientById(clientId = projectSearchResult.value.first().clientId)
            if(client != null) {
                val project = toProject(projectSearchResult.value.first())
                project.client = client
                return project
            }
            //TODO: Add Tasks
        }
        return null
    }

    @Composable
    fun findCompany(): Company? {
        val currentCompany by company.observeAsState()
        return currentCompany?.let { toCompany(it) }
    }
}