package de.mreuter.smork.ui.company.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.mreuter.smork.backend.core.Person
import de.mreuter.smork.backend.worker.domain.Worker
import de.mreuter.smork.exampleWorker
import de.mreuter.smork.ui.theme.SmorkTheme
import de.mreuter.smork.ui.utils.BottomNavigationBar
import de.mreuter.smork.ui.utils.person.PersonEditing

@Composable
fun WorkerEditing(
    worker: Worker,
    onWorkerSave: (Worker) -> Unit = {},
    onWorkerDelete: (Worker) -> Unit = {},
    backNavigation: () -> Unit = {},
    bottomBar: @Composable () -> Unit
) {
    PersonEditing(
        person = worker,
        onPersonSave = onWorkerSave as (Person) -> Unit,
        onPersonDelete = onWorkerDelete as (Person) -> Unit,
        backNavigation = backNavigation
    ) {
        bottomBar()
    }
}

@Preview
@Composable
fun PreviewWorkerEditing() {
    SmorkTheme {
        WorkerEditing(
            worker = exampleWorker[0],
            bottomBar = { BottomNavigationBar() },
        )
    }
}
