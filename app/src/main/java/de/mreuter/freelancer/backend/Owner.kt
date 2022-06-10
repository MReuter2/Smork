package de.mreuter.freelancer.backend

import java.util.*


class Owner(var fullName: Fullname, email: EmailAddress?, var phonenumber: Long? = null, var address: Address? = null): Person(fullName, email){
    override var email = email
    set(value) {
        if(value == null) throw RuntimeException("email should not be null")
        field = value
    }
}

class OwnerService(private val ownerRepository: OwnerRepository){
    fun addOwner(newOwner: Owner) = ownerRepository.save(Owner(newOwner.fullname, newOwner.email)).uuid

    fun findByID(ownerID: UUID) = ownerRepository.findByID(ownerID)

    fun findByEmail(email: EmailAddress) = ownerRepository.findByEmail(email)
}

class OwnerRepository(){
    fun findByID(id: UUID): Owner? {
        exampleOwner.forEach { if (it.uuid == id) return it }
        return null
    }

    fun findByEmail(email: EmailAddress): Owner?{
        exampleOwner.forEach { if(it.email?.equals(email) == true) return it}
        return null
    }

    fun save(person: Owner): Owner{
        exampleOwner.remove(person)
        exampleOwner.add(person)
        return person
    }

    fun delete(person: Owner){
        exampleOwner.remove(person)
    }
}