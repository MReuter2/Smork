package de.mreuter.smork.backend.worker.domain

import de.mreuter.smork.backend.core.Address
import de.mreuter.smork.backend.core.EmailAddress
import de.mreuter.smork.backend.core.Fullname
import de.mreuter.smork.backend.core.Person
import java.util.*

class Worker(
    id: UUID = UUID.randomUUID(),
    fullname: Fullname,
    phonenumber: Long? = null,
    address: Address? = null,
    emailAddress: EmailAddress? = null
): Person(id, fullname, phonenumber, address, emailAddress)