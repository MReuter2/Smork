package de.mreuter.smork.backend.client.application

import de.mreuter.smork.backend.client.domain.Client
import java.util.*

fun fromClient(client: Client): ClientEntity {
    val id = client.id.toString()
    return ClientEntity(id, client.fullname, client.phonenumber, client.address, client.emailAddress,0)
}

fun toClient(clientEntity: ClientEntity): Client {
    val id = UUID.fromString(clientEntity.id)
    return Client(id, clientEntity.fullname, clientEntity.phonenumber, clientEntity.address, clientEntity.emailAddress)
}

fun toClients(clientEntityEntities: List<ClientEntity>): List<Client>{
    val clients = mutableListOf<Client>()
    clientEntityEntities.forEach {
        clients.add(toClient(it))
    }
    return clients
}