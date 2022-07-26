package de.mreuter.smork.backend.client.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import de.mreuter.smork.backend.client.application.ClientEntity
import de.mreuter.smork.backend.project.application.ProjectEntity

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClient(clientEntity: ClientEntity)

    @Query("SELECT * FROM client WHERE clientId = :clientId")
    fun findClientById(clientId: String): List<ClientEntity>

    @Transaction
    @Query("SELECT * FROM client JOIN project ON client.clientId = project.clientId")
    fun findAllClientsWithProjects(): LiveData<Map<ClientEntity, List<ProjectEntity>>>

    @Query("DELETE FROM client WHERE clientId = :clientId")
    fun deleteClient(clientId: String)

    @Transaction
    @Query("SELECT * FROM client")
    fun findAll(): LiveData<List<ClientEntity>>
}