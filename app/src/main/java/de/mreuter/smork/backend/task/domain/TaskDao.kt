package de.mreuter.smork.backend.task.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.mreuter.smork.backend.task.application.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM task WHERE projectId = :projectId")
    fun findTasksByProjectId(projectId: String): List<TaskEntity>

    @Query("DELETE FROM task WHERE taskId = :taskId")
    fun deleteTask(taskId: String)
}