package de.mreuter.smork.backend.owner.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.mreuter.smork.backend.owner.application.OwnerEntity
import kotlinx.coroutines.*

class OwnerRepository(private val ownerDao: OwnerDao) {
    val allOwner = ownerDao.getAllOwner()
    val searchResult = MutableLiveData<OwnerEntity>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertOwner(newOwnerEntity: OwnerEntity){
        coroutineScope.launch(Dispatchers.IO){
            ownerDao.insertOwner(newOwnerEntity)
        }
    }

    fun deleteOwner(ownerEntity: OwnerEntity){
        coroutineScope.launch(Dispatchers.IO){
            ownerDao.deleteOwner(ownerEntity.id)
        }
    }

    fun findOwnerById(ownerId: String) = ownerDao.findOwnerById(ownerId)

    private fun asyncFind(ownerId: String): Deferred<LiveData<OwnerEntity>?> =
        coroutineScope.async(Dispatchers.IO){
            return@async ownerDao.findOwnerById(ownerId)
        }
}