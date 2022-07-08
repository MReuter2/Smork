package de.mreuter.smork

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import de.mreuter.smork.backend.*
import de.mreuter.smork.ui.navigation.*
import de.mreuter.smork.ui.theme.*

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
    TestData()
    Surface {
        NavigationHost(navController)
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