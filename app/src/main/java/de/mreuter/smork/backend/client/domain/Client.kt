package de.mreuter.smork.backend.client.domain

import de.mreuter.smork.backend.core.*
import de.mreuter.smork.backend.project.application.ProjectEntity
import de.mreuter.smork.backend.project.domain.Project
import java.util.*


class Client(
    id: UUID = UUID.randomUUID(),
    fullname: Fullname,
    phonenumber: Long? = null,
    address: Address? = null,
    emailAddress: EmailAddress? = null,
): Person(id, fullname, phonenumber, address, emailAddress){
    val projects = mutableListOf<Project>()
    val maintenances = mutableListOf<Maintenance>()

    fun addProjects(newProjectEntities: List<Project>){
        newProjectEntities.forEach { projects.add(it) }
    }
}