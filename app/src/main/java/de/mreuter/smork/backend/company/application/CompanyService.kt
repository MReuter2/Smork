package de.mreuter.smork.backend.company.application

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import de.mreuter.smork.backend.company.domain.Company
import de.mreuter.smork.backend.company.domain.CompanyRepository

class CompanyService(private val companyRepository: CompanyRepository) {
    private val company = companyRepository.findCompanyEntity

    fun insertCompany(companyEntity: CompanyEntity) = companyRepository.insertCompany(companyEntity)

    @Composable
    fun findCompany(): Company? {
        val currentCompany by company.observeAsState()
        return currentCompany?.let { toCompany(it) }
    }
}