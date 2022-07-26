package de.mreuter.smork.backend.owner.application

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import de.mreuter.smork.backend.core.Address
import de.mreuter.smork.backend.core.EmailAddress
import de.mreuter.smork.backend.core.Fullname
import java.util.*

@Entity(tableName = "owner")
data class OwnerEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ownerId")
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