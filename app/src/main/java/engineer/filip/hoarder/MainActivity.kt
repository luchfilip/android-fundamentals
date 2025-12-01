package engineer.filip.hoarder

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import engineer.filip.hoarder.navigation.NavGraph
import engineer.filip.hoarder.theme.HoarderTheme
import engineer.filip.hoarder.ui.Hints

/**
 * Day 2 Exercise 7: Handle share intents from other apps.
 *
 * Inject ShareHandler, handle intent in onCreate, override onNewIntent.
 *
 * Stuck? See Hints.Day2Exercise7
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // TODO: Inject ShareHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // TODO: Handle share intent

        setContent {
            HoarderTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // TODO: Override onNewIntent

    // TODO: Implement handleShareIntent

    @Suppress("unused")
    private val _hint = Hints.Day2Exercise7
}
