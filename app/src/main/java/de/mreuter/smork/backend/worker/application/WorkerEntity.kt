package de.mreuter.smork.backend.worker.application

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.mreuter.smork.backend.core.*
import de.mreuter.smork.backend.worker.domain.Worker
import java.util.*

@Entity(tableName = "worker")
data class WorkerEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "workerId")
    var id: String = UUID.randomUUID().toString(),
    @Embedded
    var fullname: Fullname,
    var phonenumber: Long? = null,
    @Embedded
    var address: Address? = null,
    @Embedded
    var emailAddress: EmailAddress? = null,
    val companyId: Int
)