package de.mreuter.freelancer.backend

import java.util.*


class Worker(var fullName: Fullname, email: EmailAddress?, val phonenumber: Long? = null, val address: Address? = null): Person(fullName, email){
    override var email = email
        set(value) {
            if(value == null) throw RuntimeException("email should not be null")
            field = value
        }
}

class WorkerService(val workerRepository: WorkerRepository){

    fun addWorker(newWorker: Worker) = workerRepository.save(Worker(newWorker.fullname, newWorker.email)).uuid

    fun removeWorker(oldWorker: Worker){
        exampleWorker.remove(oldWorker)
    }

    fun findByID(workerID: UUID) = workerRepository.findByID(workerID)

    fun findByEmail(email: EmailAddress) = workerRepository.findByEmail(email)
}

class WorkerRepository(){
    fun findByID(id: UUID): Worker? {
        exampleWorker.forEach { if (it.uuid == id) return it }
        return null
    }

    fun findByEmail(email: EmailAddress): Worker?{
        exampleWorker.forEach { if(it.email == email) return it}
        return null
    }

    fun save(person: Worker): Worker{
        exampleWorker.remove(person)
        exampleWorker.add(person)
        return person
    }

    fun delete(person: Worker){
        exampleWorker.remove(person)
    }
}