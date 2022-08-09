package de.mreuter.smork

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.mreuter.smork.backend.client.domain.Client
import de.mreuter.smork.backend.company.domain.Company
import de.mreuter.smork.backend.core.Address
import de.mreuter.smork.backend.core.EmailAddress
import de.mreuter.smork.backend.core.Fullname
import de.mreuter.smork.backend.owner.domain.Owner
import de.mreuter.smork.backend.project.domain.Project
import de.mreuter.smork.backend.worker.domain.Worker

val exampleCompanies = mutableListOf<Company>(
    Company(name = "Hermann Josef Reuter GmbH", description = "Heizungssanitär"),
    Company(name = "Example Company", description = "Example Work")
)

val exampleClients = mutableListOf<Client>(
    Client(fullname = Fullname("Angela", "Merkel"),
        phonenumber = 12345678912,
        address = Address(51688, "Wipperfürth", "Eichendorffstr", 30)
    ),
    Client(fullname = Fullname("Barack", "Obama")),
    Client(fullname = Fullname("Heidi", "Klum")),
    Client(fullname = Fullname("Jonas", "Hector")),
    Client(fullname = Fullname("Olaf", "Scholz"))
)

val exampleOwner = mutableListOf<Owner>(
    Owner(fullname = Fullname("Thomas", "Reuter"),
        emailAddress = EmailAddress("mail@mail.de")),
    Owner(fullname = Fullname("Max", "Mustermann"),
        emailAddress = EmailAddress("example@mail.go"))
)

val exampleProjects = mutableListOf<Project>(
    Project(name = "Bathroom", client = exampleClients[0]),
    Project(name = "Kitchen", client = exampleClients[0]),
    Project(name = "Boiler", client = exampleClients[0]),
    Project(name = "Downstairs", client = exampleClients[0])
)

val exampleWorker = mutableListOf<Worker>(
    Worker(fullname = Fullname("Thomas", "Gottschalk"),
        emailAddress = EmailAddress("Thomas.Gottschalk@mail.de")),
    Worker(fullname = Fullname("Dieter", "Bohlen"),
        emailAddress = EmailAddress("Dieter@Bohlen.ocm")),
    Worker(fullname = Fullname("Helene", "Fischer"),
        emailAddress = EmailAddress("Helene@Fisch.er)"))
)