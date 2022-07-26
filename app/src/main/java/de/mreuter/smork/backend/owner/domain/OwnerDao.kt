package de.mreuter.smork.backend.owner.domain

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.mreuter.smork.backend.owner.application.OwnerEntity

@Dao
interface OwnerDao {
    @Insert
    fun insertOwner(ownerEntity: OwnerEntity)

    @Query("SELECT * FROM owner WHERE ownerId = :ownerId")
    fun findOwnerById(ownerId: String): LiveData<OwnerEntity>

    @Query("DELETE FROM owner WHERE ownerId = :ownerId")
    fun deleteOwner(ownerId: String)

    @Query("SELECT * FROM owner")
    fun getAllOwner(): LiveData<List<OwnerEntity>>
}