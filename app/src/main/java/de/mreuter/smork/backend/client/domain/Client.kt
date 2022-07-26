package de.mreuter.smork.backend.client.domain

import de.mreuter.smork.backend.core.Address
import de.mreuter.smork.backend.core.EmailAddress
import de.mreuter.smork.backend.core.Fullname
import de.mreuter.smork.backend.core.Maintenance
import de.mreuter.smork.backend.project.application.ProjectEntity
import de.mreuter.smork.backend.project.domain.Project
import java.util.*


class Client(
    val id: UUID = UUID.randomUUID(),
    var fullname: Fullname,
    var phonenumber: Long? = null,
    var address: Address? = null,
    var emailAddress: EmailAddress? = null,
){
    val projects = mutableListOf<Project>()
    val maintenances = mutableListOf<Maintenance>()

    fun addProjects(newProjectEntities: List<Project>){
        newProjectEntities.forEach { projects.add(it) }
    }

    override fun toString(): String {
        return fullname.toString()
    }

    override fun equals(other: Any?): Boolean {
        if(other is Client){
            return this.id == other.id
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fullname.hashCode()
        result = 31 * result + (phonenumber?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (emailAddress?.hashCode() ?: 0)
        result = 31 * result + projects.hashCode()
        result = 31 * result + maintenances.hashCode()
        return result
    }
}