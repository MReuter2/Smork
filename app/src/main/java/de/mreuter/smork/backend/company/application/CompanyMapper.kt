package de.mreuter.smork.backend.company.application

import de.mreuter.smork.backend.company.domain.Company

fun fromCompany(company: Company): CompanyEntity = CompanyEntity(name = company.name, description = company.description)

fun toCompany(companyEntity: CompanyEntity): Company = Company(name = companyEntity.name, description = companyEntity.description)