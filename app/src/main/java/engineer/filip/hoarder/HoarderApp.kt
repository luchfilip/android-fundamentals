package engineer.filip.hoarder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Day 2 Exercise 2: Log app start. See Hints.Day2Exercise2
 */
@HiltAndroidApp
class HoarderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // TODO Exercise 2: Add Log.d("Hoarder", "Application started!")
    }
}