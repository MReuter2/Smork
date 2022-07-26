package de.mreuter.smork.backend.worker.domain

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.mreuter.smork.backend.worker.application.WorkerEntity

@Dao
interface WorkerDao {
    @Insert
    fun insertWorker(workerEntity: WorkerEntity)

    @Query("SELECT * FROM worker WHERE workerId = :workerId")
    fun findWorkerById(workerId: String): LiveData<WorkerEntity>

    @Query("DELETE FROM worker WHERE workerId = :workerId")
    fun deleteWorker(workerId: String)

    @Query("SELECT * FROM worker")
    fun getAllWorker(): LiveData<List<WorkerEntity>>
}