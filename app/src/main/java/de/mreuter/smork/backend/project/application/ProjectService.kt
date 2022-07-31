package de.mreuter.smork.backend.project.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import de.mreuter.smork.backend.client.application.ClientService
import de.mreuter.smork.backend.client.application.toClient
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.backend.project.domain.ProjectRepository
import de.mreuter.smork.backend.project.domain.TaskRepository

class ProjectService(private val projectRepository: ProjectRepository, private val taskRepository: TaskRepository, private val clientService: ClientService) {
    private val allProjectsWithClient = projectRepository.allProjectsWithClient
    private val projectSearchResults = projectRepository.searchResults
    private val taskSearchResults = taskRepository.searchResults

    fun insertProject(project: Project){
        val projectEntity = fromProject(project)
        projectRepository.insertProject(projectEntity)
        project.tasks.forEach {
            taskRepository.insertTask(fromTask(it, project.id.toString()))
        }
    }

    fun deleteProject(project: Project){
        projectRepository.deleteProject(fromProject(project))
    }

    @Composable
    fun findAllProjects(): List<Project>{
        val allProjectEntities by this.allProjectsWithClient.observeAsState()
        val allProjects = mutableListOf<Project>()
        allProjectEntities?.forEach { (projectEntity, clientEntity) ->
            val project = toProject(projectEntity)
            project.client = toClient(clientEntity)
            allProjects.add(project)
        }
        return allProjects
    }

    @Composable
    fun findProjectById(projectId: String): Project?{
        projectRepository.findProjectById(projectId)
        taskRepository.findTasksByProjectId(projectId)
        val projectSearchResult by projectSearchResults.observeAsState(listOf())
        val tasks by taskSearchResults.observeAsState(listOf())
        if(projectSearchResult.isNotEmpty() && projectSearchResult.first().id == projectId){
            val projectEntity = projectSearchResult.first()
            val client = clientService.findClientById(projectEntity.clientId)
            if(client != null) {
                val project = toProject(projectEntity)
                project.client = client
                project.addTasks(toTasks(tasks))
                return project
            }
        }
        return null
    }

    @Composable
    fun findProjectsByClientId(clientId: String): List<Project>{
        projectRepository.findProjectsByClientId(clientId)
        val projectSearchResults by this.projectSearchResults.observeAsState(listOf())
        return toProjects(projectSearchResults)
    }
}