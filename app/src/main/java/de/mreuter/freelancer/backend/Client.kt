package de.mreuter.freelancer.backend

import java.util.*


data class Client(
    override var fullname: Fullname,
    var phonenumber: Long? = null,
    var address: Address? = null,
    val id: UUID = UUID.randomUUID()
): Person(fullname){
    private val projects = mutableListOf<Project>()
    private val maintenances = mutableListOf<Maintenance>()

    fun addProject(project: Project){
        projects.add(project)
    }

    fun removeProject(project: Project){
        projects.remove(project)
    }

    fun addMaintenance(maintenance: Maintenance){
        maintenances.add(maintenance)
    }

    fun removeMaintenance(maintenance: Maintenance){
        maintenances.remove(maintenance)
    }
}


class ClientService(private val clientRepository: ClientRepository){
    fun addClient(client: Client) = clientRepository.save(client).uuid

    fun deleteByUUID(uuid: UUID){
        val client = clientRepository.findByID(uuid)
        if(client != null){
            clientRepository.delete(client)
        }else{
            throw RuntimeException("Dont know a client with ID: $uuid")
        }
    }

    fun findByID(id: UUID):Client? {
        return clientRepository.findByID(id)
    }
}

class ClientRepository(){
    fun findByID(id: UUID): Client? {
        exampleClients.forEach { if (it.uuid == id) return it }
        return null
    }

    fun save(person: Client): Client{
        exampleClients.remove(person)
        exampleClients.add(person)
        return person
    }

    fun delete(person: Client){
        exampleClients.remove(person)
    }
}