package de.mreuter.smork.backend.project.domain

import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.core.Date
import de.mreuter.smork.backend.core.Task
import java.time.LocalDate
import java.util.*

class Project (
    var id: UUID = UUID.randomUUID(),
    var name: String,
    var client: Client? = null,
    var startDate: Date? = null,
    var finishDate: Date? = null
){
    val tasks = mutableListOf<Task>()

    fun isFinished(): Boolean = finishDate != null && finishDate!!.localDate.isBefore(LocalDate.now())

    fun addTasks(newTasks: List<Task>){
        newTasks.forEach { tasks.add(it) }
    }
}