package de.mreuter.smork.backend.company.domain

import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.worker.domain.Worker
import java.security.acl.Owner

class Company (
    val name: String,
    val description: String
){
    val workerEntity = mutableListOf<Worker>()
    val owner = mutableListOf<Owner>()
    val clients = mutableListOf<Client>()

    fun addWorker(newWorker: List<Worker>){
        newWorker.forEach { workerEntity.add(it) }
    }

    fun addOwner(newOwner: List<Owner>){
        newOwner.forEach { owner.add(it) }
    }

    fun addClients(newClients: List<Client>){
        newClients.forEach { clients.add(it) }
    }
}