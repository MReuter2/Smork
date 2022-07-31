package de.mreuter.smork.backend.project.application

import de.mreuter.smork.backend.project.domain.Task
import java.util.*

fun fromTask(task: Task, projectId: String): TaskEntity = TaskEntity(task.id.toString(), task.description, task.isFinished, projectId)

fun toTask(taskEntity: TaskEntity): Task {
    val task = Task(UUID.fromString(taskEntity.id), taskEntity.description)
    task.isFinished = taskEntity.isFinished
    return task
}

fun toTasks(taskEntities: List<TaskEntity>): List<Task>{
    val tasks = mutableListOf<Task>()
    taskEntities.forEach { tasks.add(toTask(it)) }
    return tasks
}