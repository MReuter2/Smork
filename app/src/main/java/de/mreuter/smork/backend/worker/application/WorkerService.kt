package de.mreuter.smork.backend.worker.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import de.mreuter.smork.backend.worker.domain.Worker
import de.mreuter.smork.backend.worker.domain.WorkerRepository

class WorkerService(private val workerRepository: WorkerRepository) {
    private val allWorker = workerRepository.allWorker
    private val workerSearchResults = workerRepository.searchResult

    fun insertWorker(newWorker: Worker){
        workerRepository.insertWorker(fromWorker(newWorker))
    }

    fun deleteWorker(worker: Worker){
        workerRepository.deleteWorker(fromWorker(worker))
    }

    @Composable
    fun findWorkerById(workerId: String): Worker?{
        workerRepository.findWorkerById(workerId)
        val workerSearchResult by workerSearchResults.observeAsState()
        return workerSearchResult?.let { toWorker(it) }
    }

    @Composable
    fun findAllWorker(): List<Worker>{
        val allWorkerEntities by this.allWorker.observeAsState(listOf())
        val allWorker = mutableListOf<Worker>()
        allWorkerEntities.forEach { allWorker.add(toWorker(it)) }
        return allWorker
    }
}