package de.mreuter.smork.backend.owner.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import de.mreuter.smork.backend.owner.domain.Owner
import de.mreuter.smork.backend.owner.domain.OwnerRepository

class OwnerService(private val ownerRepository: OwnerRepository) {
    private val allOwner = ownerRepository.allOwner
    private val ownerSearchResults = ownerRepository.searchResult

    fun insertOwner(newOwner: Owner){
        ownerRepository.insertOwner(fromOwner(newOwner))
    }

    fun deleteOwner(owner: Owner){
        ownerRepository.deleteOwner(fromOwner(owner))
    }

    @Composable
    fun findAllOwner(): List<Owner>{
        val allOwnerEntities by this.allOwner.observeAsState(listOf())
        val allOwner = mutableListOf<Owner>()
        allOwnerEntities.forEach { allOwner.add(toOwner(it)) }
        return allOwner
    }

    @Composable
    fun findOwnerById(ownerId: String): Owner?{
        ownerRepository.findOwnerById(ownerId)
        val ownerSearchResult by ownerSearchResults.observeAsState()
        return ownerSearchResult?.let { toOwner(it) }
    }
}