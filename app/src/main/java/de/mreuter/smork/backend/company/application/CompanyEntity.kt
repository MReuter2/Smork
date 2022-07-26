package de.mreuter.smork.backend.company.application

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company")
data class CompanyEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "companyId")
    var id: Int = 0,
    val name: String,
    val description: String,
)