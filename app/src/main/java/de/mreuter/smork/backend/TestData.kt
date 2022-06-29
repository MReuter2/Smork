package de.mreuter.smork.backend

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

val exampleCompanies = mutableListOf<Company>(
    Company("Hermann Josef Reuter GmbH", "Heizungssanitär"),
    Company("Example Company", "Example Work")
)

val exampleClients = mutableListOf<Client>(
    Client(Fullname("Angela", "Merkel"), 12345678912, Address(51688, "Wipperfürth", "Eichendorffstr", 30),
        exampleCompanies[0].uuid),
    Client(Fullname("Barack", "Obama")),
    Client(Fullname("Heidi", "Klum")),
    Client(Fullname("Anthony", "Modeste")),
    Client(Fullname("Jonas", "Hector")),
    Client(Fullname("Olaf", "Scholz"))
)

val exampleOwner = mutableListOf<Owner>(
    Owner(Fullname("Thomas", "Reuter"), EmailAddress("mail@mail.de")),
    Owner(Fullname("Max", "Mustermann"), EmailAddress("example@mail.go"))
)

val exampleProjects = mutableListOf<Project>(
    Project("Bathroom", exampleClients[0]),
    Project("Kitchen", exampleClients[0]),
    Project("Boiler", exampleClients[0]),
    Project("Downstairs", exampleClients[0])
)

val exampleWorker = mutableListOf<Worker>(
    Worker(Fullname("Thomas", "Gottschalk"), EmailAddress("Thomas.Gottschalk@mail.de")),
    Worker(Fullname("Dieter", "Bohlen"), EmailAddress("Dieter@Bohlen.ocm")),
    Worker(Fullname("Helene", "Fischer"), EmailAddress("Helene@Fisch.er)"))
)

class TestData {
    init {
        exampleCompanies[0].addOwner(exampleOwner[0])
        exampleCompanies[0].addWorker(exampleWorker[0])
        exampleCompanies[0].addWorker(exampleWorker[1])
        exampleCompanies[0].addWorker(exampleWorker[2])

        exampleProjects[0].addTask(Task("Cleaning Bathroom"))
        exampleProjects[0].addTask(Task("Installing Toilet"))
        exampleProjects[0].addTask(Task("Testing Toilet"))
        exampleProjects[0].addTask(Task("Inspecting door"))
    }
}

@Composable
@Preview
fun TestApp(){

}