package de.mreuter.smork.backend.task.application

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.mreuter.smork.backend.project.application.ProjectEntity

@Entity(tableName = "task", foreignKeys = [ForeignKey(
    childColumns =["projectId"],
    parentColumns = ["projectId"],
    entity = ProjectEntity::class,
    onDelete = ForeignKey.CASCADE
)]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "taskId")
    val id: String,
    val description: String,
    val isFinished: Boolean,
    val projectId: String
)