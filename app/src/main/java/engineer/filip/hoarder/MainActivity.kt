package engineer.filip.hoarder

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import engineer.filip.hoarder.navigation.NavGraph
import engineer.filip.hoarder.theme.HoarderTheme
import engineer.filip.hoarder.ui.Hints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    override fun onResume() {
        super.onResume()
    }

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "onNewIntent: ${intent.getStringExtra(Intent.EXTRA_TEXT)}")
    }
}
