package de.mreuter.smork.backend.task.domain

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