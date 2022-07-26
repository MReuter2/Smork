package de.mreuter.smork.backend.project.application

import de.mreuter.smork.backend.core.Date
import de.mreuter.smork.backend.project.domain.Project
import java.time.LocalDate
import java.util.*

fun fromProject(project: Project): ProjectEntity {
    val projectId = project.id.toString()
    val startDate = project.startDate?.localDate?.toEpochDay()
    val finishDate = project.finishDate?.localDate?.toEpochDay()
    val clientId = project.client?.id.toString()
    return ProjectEntity(projectId, project.name, startDate, finishDate, clientId)
}

fun toProject(projectEntity: ProjectEntity): Project {
    val getStartDate: () -> Date? =
        { if (projectEntity.startDate != null) Date(LocalDate.ofEpochDay(projectEntity.startDate ?: 0)) else projectEntity.startDate }
    val getFinishDate: () -> Date? =
        { if (projectEntity.finishDate != null) Date(LocalDate.ofEpochDay(projectEntity.finishDate ?: 0)) else projectEntity.finishDate }
    return Project(
        id = UUID.fromString(projectEntity.id),
        name = projectEntity.name,
        startDate = getStartDate(),
        finishDate = getFinishDate()
    )
}

fun toProjects(projectEntities: List<ProjectEntity>): List<Project>{
    val projects = mutableListOf<Project>()
    projectEntities.forEach {
        projects.add(toProject(it))
    }
    return projects
}