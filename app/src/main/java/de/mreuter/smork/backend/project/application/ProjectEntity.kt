package de.mreuter.smork.backend.project.application

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "project")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "projectId")
    var id: String = UUID.randomUUID().toString(),
    var name: String,
    var startDate: Long? = null,
    var finishDate: Long? = null,
    var clientId: String
)