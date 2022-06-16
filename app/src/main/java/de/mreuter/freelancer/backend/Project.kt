package de.mreuter.freelancer.backend

import android.media.Image
import java.util.*


class Project(var name: String, val client: Client, val tasks: MutableList<Task> = mutableListOf()): AbstractEntity() {
    var isFinished = false
    var startDate: Date? = null
    var finishDate: Date? = null
    var images = mutableListOf<Image>()

    fun finish(startDate: Date, finishDate: Date){
        if(startDate.before(finishDate)) {
            this.startDate = startDate
            this.finishDate = finishDate
            isFinished = true
        }else{
            throw RuntimeException("Startdate should be before finishdate")
        }
    }

    fun addTask(task: Task){
        tasks.add(task)
    }

    fun deleteTask(task: Task){
        if(!tasks.remove(task)) throw RuntimeException("This task is not inside the tasklist")
    }

    fun addImage(image: Image){
        images.add(image)
    }

    fun deleteImage(image: Image){
        if(!images.remove(image)) throw RuntimeException("This image is not in the imagestore")
    }
}

class ProjectService(val projectRepository: ProjectRepository){

    /*TODO:Image Upload*/

    fun addTask(projectId: UUID, task: Task){
        val project = findByID(projectId)
        project.addTask(task)
        save(project)
    }

    fun deleteTask(projectId: UUID, task: Task){
        val project = findByID(projectId)
        project.deleteTask(task)
        save(project)
    }

    fun addProject(project: Project): UUID{
        return projectRepository.save(project).uuid
    }

    fun finishProject(project: Project, startDate: Date, endDate: Date){
        project.finish(startDate, endDate)
        save(project)
    }

    fun findByID(id: UUID): Project {
        return projectRepository.findByID(id)
            ?: throw RuntimeException("Dont know the project with ID: $id")
    }

    fun deleteByID(id: UUID){
        val project = findByID(id)
        projectRepository.delete(project)
    }

    fun save(project: Project){
        projectRepository.save(project)
    }
}

class ProjectRepository(){
    fun findByID(id: UUID): Project? {
        exampleProjects.forEach { if (it.uuid == id) return it }
        return null
    }

    fun save(project: Project): Project{
        exampleProjects.remove(project)
        exampleProjects.add(project)
        return project
    }

    fun delete(project: Project){
        exampleProjects.remove(project)
    }
}