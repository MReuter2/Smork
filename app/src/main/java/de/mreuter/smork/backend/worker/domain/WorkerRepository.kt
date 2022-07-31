package de.mreuter.smork.backend.worker.domain

import androidx.lifecycle.MutableLiveData
import de.mreuter.smork.backend.worker.application.WorkerEntity
import kotlinx.coroutines.*

class WorkerRepository(private val workerDao: WorkerDao) {
    val allWorker = workerDao.findAllWorker()
    val searchResult = MutableLiveData<WorkerEntity>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertWorker(newWorkerEntity: WorkerEntity){
        coroutineScope.launch(Dispatchers.IO){
            workerDao.insertWorker(newWorkerEntity)
        }
    }

    fun deleteWorker(workerEntity: WorkerEntity){
        coroutineScope.launch(Dispatchers.IO){
            workerDao.deleteWorker(workerEntity.id)
        }
    }

    fun findWorkerById(workerId: String) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResult.value = asyncFind(workerId).await()
        }
    }

    private fun asyncFind(workerId: String): Deferred<WorkerEntity> =
        coroutineScope.async(Dispatchers.IO){
            return@async workerDao.findWorkerById(workerId)
        }
}