package de.mreuter.smork.backend

import android.os.Build
import androidx.annotation.RequiresApi
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

    /*override fun equals(other: Any?): Boolean {
        if(other is Task)
            return taskDescription.trim().uppercase() == other.taskDescription.trim().uppercase()
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = taskDescription.hashCode()
        result = 31 * result + isFinished.hashCode()
        return result
    }*/
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

class Maintenance(val client: Client, val description: String, val date: Date){
    var isFinished = false

    fun finish(){
        isFinished = true
    }
}

class Date(val localDate: LocalDate){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String {
        return localDate.format(DateTimeFormatter.ofPattern("dd.MM.uuuu")).toString()
    }
}