package de.mreuter.smork.backend

import de.mreuter.smork.stateHolder
import java.util.*

class Stateholder{
    private val projectService = ProjectService(ProjectRepository())
    private val ownerService = OwnerService(OwnerRepository())
    private val workerService = WorkerService(WorkerRepository())
    private val clientService = ClientService(ClientRepository())
    private val companyService = CompanyService(CompanyRepository(), projectService, clientService, workerService, ownerService)
    var user: Person? = exampleOwner[0]

    fun usersCompany() = companyService.findByPersonID(user?.uuid ?: throw RuntimeException("User is null"))

    fun findPersonByEmail(emailAddress: EmailAddress) = ownerService.findByEmail(emailAddress) ?: workerService.findByEmail(emailAddress)

    fun getClients() = companyService.getClients(usersCompany()?.uuid ?: throw RuntimeException("Company needs to be set"))

    fun getClientByID(clientID: UUID) = clientService.findByID(clientID)

    fun getWorkerByID(workerID: UUID) = workerService.findByID(workerID)

    fun getOwnerByID(ownerID: UUID) = ownerService.findByID(ownerID)

    fun getOwner() = companyService.getOwner(usersCompany()?.uuid ?: throw RuntimeException("Company should not be null"))

    fun getWorker() = companyService.getWorker( usersCompany()?.uuid ?: throw RuntimeException("Company should not be null"))

    fun getProjects() = companyService.getProjects(usersCompany()?.uuid ?: throw RuntimeException("Company should not be null"))

    fun getMaintenances() = companyService.getMaintenances(usersCompany()?.uuid ?: throw RuntimeException("Company should not be null"))

    fun getCompanyByID(id: UUID) = companyService.findByID(id)

    fun saveCompany(company: Company){
        companyService.save(company)
    }

    fun saveProject(project: Project){
        projectService.save(project)
    }

    fun saveClient(client: Client){
        if(usersCompany()?.clients?.contains(client) == true){
            companyService.removeClient(usersCompany()?.uuid ?: throw RuntimeException("Company should not be null"), client)
        }
        companyService.addClient(usersCompany()?.uuid ?: throw RuntimeException("Mistake"), client)
    }

    fun updateProject(project: Project, updatedProject: Project){
        projectService.update(project, updatedProject)
    }

    fun deleteProject(project: Project){
        projectService.deleteByID(project.uuid)
    }

    fun getProjectByID(projectID: UUID) = projectService.findByID(projectID)
}