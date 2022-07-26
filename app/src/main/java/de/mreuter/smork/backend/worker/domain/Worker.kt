package de.mreuter.smork.backend.worker.domain

import de.mreuter.smork.backend.core.Address
import de.mreuter.smork.backend.core.EmailAddress
import de.mreuter.smork.backend.core.Fullname
import java.util.*

class Worker(
    val id: UUID = UUID.randomUUID(),
    var fullname: Fullname,
    var phonenumber: Long? = null,
    var address: Address? = null,
    var emailAddress: EmailAddress? = null
)