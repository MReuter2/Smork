package de.mreuter.smork.backend.client.domain

import de.mreuter.smork.backend.core.*
import java.util.*


class Client(
    id: UUID = UUID.randomUUID(),
    fullname: Fullname,
    phonenumber: Long? = null,
    address: Address? = null,
    emailAddress: EmailAddress? = null,
): Person(id, fullname, phonenumber, address, emailAddress)