package de.mreuter.smork.backend.project.domain

import java.util.*

class Task(
    val id: UUID = UUID.randomUUID(),
    val description: String
) {
    var isFinished = false

    override fun equals(other: Any?): Boolean {
        if(other is Task){
            return other.id == id
        }
        return false
    }
}