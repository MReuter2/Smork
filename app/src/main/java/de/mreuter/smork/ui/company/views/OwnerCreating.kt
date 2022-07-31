package de.mreuter.smork.ui.company.views

import androidx.compose.runtime.Composable
import de.mreuter.smork.backend.owner.domain.Owner
import de.mreuter.smork.ui.utils.person.PersonCreating

@Composable
fun OwnerCreating(
    onOwnerSave: (Owner) -> Unit,
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
){
    PersonCreating(
        onPersonSave = {fullname, address, emailAddress, phonenumber ->
            onOwnerSave(Owner(fullname = fullname, address = address, emailAddress = emailAddress, phonenumber = phonenumber))
        },
        backNavigation = backNavigation
    ) {
        bottomBar()
    }
}