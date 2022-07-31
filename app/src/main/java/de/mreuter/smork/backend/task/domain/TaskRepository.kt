package de.mreuter.smork.backend.task.domain

import androidx.lifecycle.MutableLiveData
import de.mreuter.smork.backend.task.application.TaskEntity
import kotlinx.coroutines.*

class TaskRepository(private val taskDao: TaskDao) {
    val searchResults = MutableLiveData<List<TaskEntity>>(listOf())
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertTask(newTaskEntity: TaskEntity){
        coroutineScope.launch(Dispatchers.IO){
            taskDao.insertTask(newTaskEntity)
        }
    }

    fun insertTasks(newTaskEntities: List<TaskEntity>){
        newTaskEntities.forEach { insertTask(it) }
    }

    fun deleteTask(taskEntity: TaskEntity){
        coroutineScope.launch(Dispatchers.IO){
            taskDao.deleteTask(taskEntity.id)
        }
    }

    fun findTasksByProjectId(projectId: String){
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind { taskDao.findTasksByProjectId(projectId) }.await()
        }
    }

    private fun asyncFind(function: () -> List<TaskEntity>): Deferred<List<TaskEntity>?> =
        coroutineScope.async(Dispatchers.IO){
            return@async function()
        }
}