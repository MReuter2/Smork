package de.mreuter.smork.backend

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.util.*

class Company(var name: String, var description: String = ""): AbstractEntity() {
    val clients = mutableListOf<Client>()
    val worker = mutableListOf<Worker>()
    val owner = mutableListOf<Owner>()
    val projects = mutableListOf<Project>()
    val maintenances = mutableListOf<Maintenance>()

    fun degradeOwner(oldOwner: Owner){
        val newWorker = Worker(oldOwner.fullName, oldOwner.email, oldOwner.phonenumber, oldOwner.address)
        worker.add(newWorker)
        owner.remove(oldOwner)
    }

    fun promoteWorker(oldWorker: Worker){
        val newOwner = Owner(oldWorker.fullName, oldWorker.email, oldWorker.phonenumber, oldWorker.address)
        owner.add(newOwner)
        worker.remove(oldWorker)
    }

    fun containsPersonWithID(personID: UUID): Boolean{
        worker.forEach{ if(it.uuid == personID) return true }
        owner.forEach{ if(it.uuid == personID) return true }
        clients.forEach{ if(it.uuid == personID) return true }
        return false
    }


    fun addClient(newClient: Client){
        clients.add(newClient)
    }

    fun removeClient(client: Client){
        clients.remove(client)
    }

    fun addOwner(newOwner: Owner){
        owner.add(newOwner)
    }

    fun removeOwner(oldOwner: Owner){
        owner.remove(oldOwner)
    }

    fun addWorker(newWorker: Worker){
        worker.add(newWorker)
    }

    fun removeWorker(oldWorker: Worker){
        worker.remove(oldWorker)
    }

    fun addProject(newProject: Project){
        projects.add(newProject)
    }

    fun removeProject(project: Project){
        projects.remove(project)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finishProject(project: Project, startDate: Date, endDate: Date){
        if(projects.contains(project)){
            project.finish(startDate,endDate)
        }else{
            throw RuntimeException("Wrong project")
        }
    }

    fun addMaintenance(newMaintenance: Maintenance){
        maintenances.add(newMaintenance)
    }

    fun removeMaintenance(maintenance: Maintenance){
        maintenances.remove(maintenance)
    }

    fun finishMaintenance(maintenance: Maintenance){
        if(maintenances.contains(maintenance)){
            maintenance.finish()
        }else{
            throw RuntimeException("Wrong maintenance")
        }
    }
}

class CompanyService(val companyRepository: CompanyRepository, val projectService: ProjectService, val clientService: ClientService, val workerService: WorkerService, val ownerService: OwnerService){

    fun changeOfRank(companyID: UUID, person: Person){
        val company = findByID(companyID) ?: throw RuntimeException("Dont know a company with ID: $companyID")
        if(person is Worker){
            company.promoteWorker(person)
        }else
            if(person is Owner){
                company.degradeOwner(person)
            }else{
                throw RuntimeException("Client cant get a promote or degrade")
            }
    }

    fun addCompany(company: Company){
        companyRepository.save(company)
    }

    fun addProject(companyID: UUID, project: Project){
        val company = findByID(companyID) ?: throw RuntimeException("Dont know a company with ID: $companyID")
        projectService.addProject(project)
        company.addProject(project)
    }

    fun addOwner(companyID: UUID, owner: Owner){
        val company = findByID(companyID)?: throw RuntimeException("Dont know a company with ID: $companyID")
        ownerService.addOwner(owner)
        company.addOwner(owner)
    }

    fun addClient(companyID: UUID, client: Client){
        val company = findByID(companyID)?: throw RuntimeException("Dont know a company with ID: $companyID")
        clientService.addClient(client)
        company.addClient(client)
    }

    fun addWorker(companyID: UUID, worker: Worker){
        val company = findByID(companyID)?: throw RuntimeException("Dont know a company with ID: $companyID")
        workerService.addWorker(worker)
        company.addWorker(worker)
    }

    fun removeClient(companyID: UUID, client: Client){
        val company = findByID(companyID)?: throw RuntimeException("Dont know a company with ID: $companyID")
        clientService.deleteByUUID(client.uuid)
        company.removeClient(client)
    }

    fun getClients(companyID: UUID) = findByID(companyID)?.clients ?: throw RuntimeException("Dont know a company with ID: $companyID")

    fun getWorker(companyID: UUID) = findByID(companyID)?.worker ?: throw RuntimeException("Dont know a company with ID: $companyID")

    fun getOwner(companyID: UUID) = findByID(companyID)?.owner ?: throw RuntimeException("Dont know a company with ID: $companyID")

    fun getProjects(companyID: UUID) = findByID(companyID)?.projects ?: throw RuntimeException("Dont know a company with ID: $companyID")

    fun getMaintenances(companyID: UUID) = findByID(companyID)?.maintenances ?: throw RuntimeException("Dont know a company with ID: $companyID")

    fun save(company: Company) = companyRepository.save(company)

    fun findByID(companyID: UUID) = companyRepository.findByID(companyID)

    fun findByPersonID(personID: UUID) = companyRepository.findByUserID(personID)
}

class CompanyRepository(){
    fun findByID(uuid: UUID): Company? {
        exampleCompanies.forEach { if (it.uuid == uuid) return it }
        return null
    }

    fun findByUserID(userID: UUID): Company? {
        exampleCompanies.forEach { if(it.containsPersonWithID(userID)) return it }
        return null
    }

    fun save(company: Company): Company{
        val oldCompany = findByID(company.uuid)
        if(oldCompany != null){
            exampleCompanies.remove(oldCompany)
            exampleCompanies.add(company)
        }else{
            exampleCompanies.add(company)
        }
        return company
    }

    fun delete(company: Company){
        exampleCompanies.remove(company)
    }
}