package de.mreuter.smork.backend.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.mreuter.smork.backend.client.application.ClientEntity
import de.mreuter.smork.backend.client.domain.ClientDao
import de.mreuter.smork.backend.company.application.CompanyEntity
import de.mreuter.smork.backend.company.domain.CompanyDao
import de.mreuter.smork.backend.owner.application.OwnerEntity
import de.mreuter.smork.backend.owner.domain.OwnerDao
import de.mreuter.smork.backend.project.application.ProjectEntity
import de.mreuter.smork.backend.project.domain.ProjectDao
import de.mreuter.smork.backend.project.application.TaskEntity
import de.mreuter.smork.backend.project.domain.TaskDao
import de.mreuter.smork.backend.worker.application.WorkerEntity
import de.mreuter.smork.backend.worker.domain.WorkerDao

@Database(
    entities = [(ProjectEntity::class), (CompanyEntity::class), (OwnerEntity::class), (WorkerEntity::class), (ClientEntity::class), (TaskEntity::class)],
    version = 8, exportSchema = true)
abstract class SmorkDatabase: RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun companyDao(): CompanyDao
    abstract fun ownerDao(): OwnerDao
    abstract fun workerDao(): WorkerDao
    abstract fun clientDao(): ClientDao
    abstract fun taskDao(): TaskDao

    companion object {
        private var INSTANCE: SmorkDatabase? = null

        fun getInstance(context: Context): SmorkDatabase {
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SmorkDatabase::class.java,
                    "smork_db"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }
    }

}