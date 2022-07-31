package de.mreuter.smork.ui.company.views

import androidx.compose.runtime.Composable
import de.mreuter.smork.backend.worker.domain.Worker
import de.mreuter.smork.ui.utils.person.PersonCreating

@Composable
fun WorkerCreating(
    onWorkerSave: (Worker) -> Unit,
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
){
    PersonCreating(
        onPersonSave = {fullname, address, emailAddress, phonenumber ->
            onWorkerSave(Worker(fullname = fullname, address = address, emailAddress = emailAddress, phonenumber = phonenumber))
        },
        backNavigation = backNavigation
    ) {
        bottomBar()
    }
}