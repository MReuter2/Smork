package de.mreuter.smork.backend.company.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import de.mreuter.smork.backend.company.application.CompanyEntity

@Dao
interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompany(companyEntity: CompanyEntity)

    @Query("DELETE FROM company WHERE companyId = 0")
    fun deleteCompany()

    @Transaction
    @Query("SELECT * FROM company")
    fun findCompany(): LiveData<CompanyEntity>
}