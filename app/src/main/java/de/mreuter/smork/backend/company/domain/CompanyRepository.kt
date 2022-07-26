package de.mreuter.smork.backend.company.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.mreuter.smork.backend.company.application.CompanyEntity
import kotlinx.coroutines.*

class CompanyRepository(private val companyDao: CompanyDao) {
    val searchResult = MutableLiveData<CompanyEntity>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertCompany(newCompanyEntity: CompanyEntity){
        coroutineScope.launch(Dispatchers.IO){
            companyDao.insertCompany(newCompanyEntity)
        }
    }

    fun deleteCompany(){
        coroutineScope.launch(Dispatchers.IO){
            companyDao.deleteCompany()
        }
    }

    val findCompanyEntity: LiveData<CompanyEntity> = companyDao.findCompany()
}