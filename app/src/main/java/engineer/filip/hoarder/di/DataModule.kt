package engineer.filip.hoarder.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import engineer.filip.hoarder.ui.Hints

/**
 * Hilt module for providing concrete data dependencies.
 *
 * Day 2 Exercise 10: Add a @Provides function for SharedPreferences.
 *
 * Stuck? See Hints.Day2Exercise10
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // TODO: Add @Provides @Singleton function for SharedPreferences

    // Hint reference
    @Suppress("unused")
    private val _hint = Hints.Day2Exercise10
}
