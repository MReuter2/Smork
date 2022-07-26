package de.mreuter.smork.backend.core

import de.mreuter.smork.backend.client.application.ClientEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Address(var postcode: Int, var city: String, var street: String, var houseNumber: Int){
    init {
        city = city.trim()
        street = street.trim()
    }

    override fun toString(): String {
        return "$street $houseNumber, $postcode $city"
    }
}

data class Fullname(var firstname: String, var lastname: String){
    init {
        firstname = firstname.trim()
        lastname = lastname.trim()
    }

    override fun toString(): String {
        return "$lastname, $firstname"
    }
}

class Task(val taskDescription: String){
    var isFinished = false
}

class EmailAddress(emailAddress: String){
    var emailAddress = emailAddress
    set(value) {
        if(emailAddress.contains("@"))
            field = value
    }

    override fun equals(other: Any?): Boolean {
        if(other is EmailAddress)
            return emailAddress == other.emailAddress
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return emailAddress.hashCode()
    }

    override fun toString(): String {
        return emailAddress
    }
}

class Maintenance(val clientEntity: ClientEntity, val description: String, val date: Date){
    var isFinished = false

    fun finish(){
        isFinished = true
    }
}

class Date(val localDate: LocalDate){
    override fun toString(): String {
        return localDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu")).toString()
    }
}