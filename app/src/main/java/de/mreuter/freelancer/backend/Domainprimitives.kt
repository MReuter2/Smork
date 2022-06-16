package de.mreuter.freelancer.backend

import androidx.compose.ui.text.toUpperCase
import java.util.*

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

    override fun equals(other: Any?): Boolean {
        if(other is Task)
            return taskDescription.trim().uppercase() == other.taskDescription.trim().uppercase()
        return super.equals(other)
    }

    fun refinish(){
        isFinished = false
    }
}

class EmailAddress(emailAddress: String){
    var emailAddress = emailAddress
    set(value) {
        if(emailAddress.contains("@"))
            field = value
    }
}

class Maintenance(val client: Client, val description: String, val date: Date){
    var isFinished = false

    fun finish(){
        isFinished = true
    }
}