package de.mreuter.smork.backend.client.domain

import androidx.lifecycle.MutableLiveData
import de.mreuter.smork.backend.client.application.ClientEntity
import kotlinx.coroutines.*

class ClientRepository(private val clientDao: ClientDao) {
    val allClientEntities = clientDao.findAll()
    val searchResults = MutableLiveData<List<ClientEntity>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertClient(newClientEntityEntity: ClientEntity){
        coroutineScope.launch(Dispatchers.IO){
            clientDao.insertClient(newClientEntityEntity)
        }
    }

    fun deleteClient(clientEntity: ClientEntity){
        coroutineScope.launch(Dispatchers.IO){
            clientDao.deleteClient(clientEntity.id)
        }
    }

    fun findClientById(clientId: String){
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFindClient(clientId).await()
        }
    }
    private fun asyncFindClient(clientId: String): Deferred<List<ClientEntity>> =
        coroutineScope.async(Dispatchers.IO){
            return@async clientDao.findClientById(clientId)
        }
}