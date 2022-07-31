package de.mreuter.smork.ui.company.views

import androidx.compose.runtime.Composable
import de.mreuter.smork.backend.core.Person
import de.mreuter.smork.backend.owner.domain.Owner
import de.mreuter.smork.ui.utils.person.PersonEditing

@Composable
fun OwnerEditing(
    owner: Owner,
    onOwnerSave: (Owner) -> Unit = {},
    onOwnerDelete: (Owner) -> Unit = {},
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    PersonEditing(
        person = owner,
        onPersonSave = onOwnerSave as (Person) -> Unit,
        onPersonDelete = onOwnerDelete as (Person) -> Unit,
        backNavigation = backNavigation
    ) {
        bottomBar()
    }
}