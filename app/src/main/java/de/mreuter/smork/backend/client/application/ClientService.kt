package de.mreuter.smork.backend.client.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.client.domain.ClientRepository

class ClientService(private val clientRepository: ClientRepository) {
    private val allClients = clientRepository.allClientEntities
    private val clientSearchResults = clientRepository.searchResults

    fun insertClient(newClient: Client){
        clientRepository.insertClient(fromClient(newClient))
    }

    fun deleteClient(client: Client){
        clientRepository.deleteClient(fromClient(client))
    }

    @Composable
    fun findClientById(clientId: String): Client?{
        clientRepository.findClientById(clientId)
        val clientSearchResult by clientSearchResults.observeAsState(listOf())
        if(clientSearchResult.isNotEmpty() && clientSearchResult.first().id == clientId) {
            val result = clientSearchResults.value?.first()
            return result?.let { toClient(it) }
        }
        return null
    }

    @Composable
    fun findAllClients(): List<Client>{
        val allClients by this.allClients.observeAsState(listOf())
        return toClients(allClients)
    }
}