package de.mreuter.smork

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import de.mreuter.smork.backend.client.application.fromClient
import de.mreuter.smork.backend.company.application.CompanyEntity
import de.mreuter.smork.backend.database.SmorkDatabase
import de.mreuter.smork.backend.project.application.fromProject
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

abstract class DbTest {
    protected lateinit var smorkDatabase: SmorkDatabase

    @Before
    fun initDb() {
        smorkDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SmorkDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        smorkDatabase.close()
    }
}

@RunWith(AndroidJUnit4::class)
@LargeTest
class SimpleEntityReadWriteTest: DbTest() {

    @Test
    @Throws(Exception::class)
    @UiThreadTest
    fun insertProjectTest() {
        val project = exampleProjects[0]
        smorkDatabase.projectDao().insertProject(fromProject(project))
        val foundProjectWrappedInLiveData = smorkDatabase.projectDao().findProjectById(project.id.toString())
        val foundProject = foundProjectWrappedInLiveData.first()
        assertThat(foundProject, equalTo(project))
    }

    @Test
    @Throws(Exception::class)
    @UiThreadTest
    fun deleteProjectTest() {
        val project = exampleProjects[0]
        smorkDatabase.projectDao().insertProject(fromProject(project))
        val foundProjectWrappedInLiveData = smorkDatabase.projectDao().findProjectById(project.id.toString())
        val foundProject = foundProjectWrappedInLiveData.first()
        smorkDatabase.projectDao().deleteProjectById(foundProject.id)
        val foundProjectAfterDeletion = smorkDatabase.projectDao().findProjectById(project.id.toString())
        assertEquals(true, foundProjectAfterDeletion.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    @UiThreadTest
    fun findClientTest() {
        val client = exampleClients[0]
        smorkDatabase.clientDao().insertClient(fromClient(client))
        val foundClient = smorkDatabase.clientDao().findClientById(client.id.toString())
        assertEquals(client.id, foundClient.first().id)
    }

    @Test
    @Throws(Exception::class)
    @UiThreadTest
    fun writeClientsAndReadAllClients(){
        val client1 = fromClient(exampleClients[0])
        val client2 = fromClient(exampleClients[1])
        val client3 = fromClient(exampleClients[2])
        smorkDatabase.clientDao().insertClient(client1)
        smorkDatabase.clientDao().insertClient(client2)
        smorkDatabase.clientDao().insertClient(client3)
        val clientsWrappedInLiveData = smorkDatabase.clientDao().findAll()
        val clients = clientsWrappedInLiveData.getValueBlocking()
        assert(clients?.size == 3) { "Number of Clients: ${clients?.size}" }
        assertThat(clients?.get(1), equalTo(exampleClients[1]))
    }

    @Test
    @Throws(Exception::class)
    fun getCurrentCompanyTest(){
        val companyEntity = CompanyEntity(name = "Test", description = "Test")
        smorkDatabase.companyDao().insertCompany(companyEntity)
        val foundCompany = smorkDatabase.companyDao().findCompany()
        assertEquals(companyEntity, foundCompany)
    }

    @Throws(InterruptedException::class)
    fun <T> LiveData<T>.getValueBlocking(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val innerObserver = Observer<T> {
            value = it
            latch.countDown()
        }
        observeForever(innerObserver)
        latch.await(4, TimeUnit.SECONDS)
        return value
    }
}