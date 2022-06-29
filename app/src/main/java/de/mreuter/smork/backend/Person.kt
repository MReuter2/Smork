package de.mreuter.smork.backend

import java.util.*

abstract class Person(open var fullname: Fullname, open var email: EmailAddress? = null, id: UUID = UUID.randomUUID()): AbstractEntity(id)
