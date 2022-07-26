package de.mreuter.smork.backend.worker.application

import de.mreuter.smork.backend.worker.domain.Worker
import java.util.*

fun fromWorker(worker: Worker): WorkerEntity {
    val id = worker.id.toString()
    return WorkerEntity(id, worker.fullname, worker.phonenumber, worker.address, worker.emailAddress,0)
}

fun toWorker(workerEntity: WorkerEntity): Worker {
    val id = UUID.fromString(workerEntity.id)
    return Worker(id, workerEntity.fullname, workerEntity.phonenumber, workerEntity.address, workerEntity.emailAddress)
}