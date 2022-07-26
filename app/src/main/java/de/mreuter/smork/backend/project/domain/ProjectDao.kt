package de.mreuter.smork.backend.project.domain

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.mreuter.smork.backend.client.application.ClientEntity
import de.mreuter.smork.backend.project.application.ProjectEntity

@Dao
interface ProjectDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProject(projectEntity: ProjectEntity)

    @Query("SELECT * FROM project WHERE projectId = :projectId")
    fun findProjectById(projectId: String): List<ProjectEntity>

    @Query("SELECT * FROM project WHERE clientId = :clientId")
    fun findProjectByClientId(clientId: String): List<ProjectEntity>

    @Query("DELETE FROM project WHERE projectId = :projectId")
    fun deleteProjectById(projectId: String)

    @Query("SELECT * FROM project")
    fun findAllProjects(): LiveData<List<ProjectEntity>>

    @Query("SELECT * FROM client JOIN project ON client.clientId = project.clientId")
    fun findAllProjectsWithClient(): LiveData<Map<ProjectEntity, ClientEntity>>
}