package de.mreuter.smork.backend.client.application

import androidx.room.*
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.core.*
import java.util.*

@Entity(tableName = "client")
data class ClientEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "clientId")
    var id: String = UUID.randomUUID().toString(),
    @Embedded
    var fullname: Fullname,
    var phonenumber: Long? = null,
    @Embedded
    var address: Address? = null,
    @Embedded
    var emailAddress: EmailAddress? = null,
    val companyId: Int
){
    override fun toString(): String {
        return fullname.toString()
    }
}