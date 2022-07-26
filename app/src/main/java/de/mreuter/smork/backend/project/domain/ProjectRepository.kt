package de.mreuter.smork.backend.project.domain

import androidx.lifecycle.MutableLiveData
import de.mreuter.smork.backend.project.application.ProjectEntity
import kotlinx.coroutines.*

class ProjectRepository(private val projectDao: ProjectDao) {
    val searchResults = MutableLiveData<List<ProjectEntity>>(listOf())
    val allProjects = projectDao.findAllProjects()
    val allProjectsWithClient = projectDao.findAllProjectsWithClient()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertProject(newProjectEntity: ProjectEntity){
        coroutineScope.launch(Dispatchers.IO){
            projectDao.insertProject(newProjectEntity)
        }
    }

    fun deleteProject(projectEntity: ProjectEntity){
        coroutineScope.launch(Dispatchers.IO){
            projectDao.deleteProjectById(projectEntity.id)
        }
    }

    fun findProjectById(projectId: String){
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind { projectDao.findProjectById(projectId) }.await()
        }
    }

    fun findProjectsByClientId(clientId: String){
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind { projectDao.findProjectByClientId(clientId) }.await()
        }
    }

    private fun asyncFind(function: () -> List<ProjectEntity>): Deferred<List<ProjectEntity>?> =
        coroutineScope.async(Dispatchers.IO){
            return@async function()
        }
}