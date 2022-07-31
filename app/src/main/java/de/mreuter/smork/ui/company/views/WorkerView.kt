package de.mreuter.smork.ui.company.views

import androidx.compose.runtime.Composable
import de.mreuter.smork.backend.core.Person
import de.mreuter.smork.backend.worker.domain.Worker
import de.mreuter.smork.ui.utils.person.PersonInfo

@Composable
fun WorkerView(
    worker: Worker,
    navigateToEditView: (Worker) -> Unit = {},
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    PersonInfo(
        person = worker,
        navigateToEditView = navigateToEditView as (Person) -> Unit,
        backNavigation = backNavigation,
        bottomBar = bottomBar
    )
}