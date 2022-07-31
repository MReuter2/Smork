package de.mreuter.smork.ui.company.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.mreuter.smork.R
import de.mreuter.smork.backend.core.Person
import de.mreuter.smork.backend.owner.domain.Owner
import de.mreuter.smork.ui.utils.*
import de.mreuter.smork.ui.utils.person.PersonInfo

@Composable
fun OwnerView(
    owner: Owner,
    navigateToEditView: (Owner) -> Unit = {},
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    PersonInfo(
        person = owner,
        navigateToEditView = navigateToEditView as (Person) -> Unit,
        backNavigation = backNavigation,
        bottomBar = bottomBar
    )
}