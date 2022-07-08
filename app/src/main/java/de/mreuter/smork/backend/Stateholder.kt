package de.mreuter.smork.backend

import java.util.*

class Stateholder{
    private val projectService = ProjectService(ProjectRepository())
    private val ownerService = OwnerService(OwnerRepository())
    private val workerService = WorkerService(WorkerRepository())
    private val clientService = ClientService(ClientRepository())
    private val companyService = CompanyService(CompanyRepository(), projectService, clientService, workerService, ownerService)
    var user: Person? = null

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
        companyService.addClient(usersCompany()?.uuid ?: throw RuntimeException("Mistake"), client)
    }

    fun getProjectByID(projectID: UUID) = projectService.findByID(projectID)
}