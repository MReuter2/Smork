package de.mreuter.smork.backend.owner.application

import de.mreuter.smork.backend.owner.domain.Owner
import java.util.*

fun fromOwner(owner: Owner): OwnerEntity {
    val id = owner.id.toString()
    return OwnerEntity(id, owner.fullname, owner.phonenumber, owner.address, owner.emailAddress,0)
}

fun toOwner(ownerEntity: OwnerEntity): Owner {
    val id = UUID.fromString(ownerEntity.id)
    return Owner(id, ownerEntity.fullname, ownerEntity.phonenumber, ownerEntity.address, ownerEntity.emailAddress)
}