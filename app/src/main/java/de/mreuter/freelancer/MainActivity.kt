package de.mreuter.freelancer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.mreuter.freelancer.backend.*
import de.mreuter.freelancer.ui.navigation.*
import de.mreuter.freelancer.ui.screens.*
import de.mreuter.freelancer.ui.theme.*
import java.util.*

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreelancerTheme {
                Surface(
                    color = BackgroundColor
                ) {
                    MainContent()
                }
            }
        }
    }
}

val stateHolder = Stateholder()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent() {
    val navController = rememberNavController()

    Surface {
        NavigationHost(navController)
    }
}

@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                "Smork",
                color = White
            )
        },
        backgroundColor = Purple
    )
}

@Composable
fun BottomBar() {
    BottomAppBar(
        backgroundColor = Purple,
        content = {}
    )
}

@Preview
@Composable
fun PreviewTopBar() {
    FreelancerTheme {
        TopBar()
    }
}

@Preview
@Composable
fun PreviewBottomBar() {
    FreelancerTheme {
        BottomBar()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun MainContentPreview() {
    FreelancerTheme {
        MainContent()
    }
}