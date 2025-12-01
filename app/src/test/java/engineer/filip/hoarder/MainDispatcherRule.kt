package engineer.filip.hoarder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit Rule for testing coroutines that use Dispatchers.Main.
 *
 * Day 3 Exercise 4: Students learn about this test rule.
 *
 * Problem: Dispatchers.Main doesn't exist outside of Android.
 * Solution: Replace it with a test dispatcher during tests.
 *
 * This rule:
 * 1. Before each test: Sets Dispatchers.Main to a test dispatcher
 * 2. After each test: Resets Dispatchers.Main
 *
 * Usage:
 * ```
 * @get:Rule
 * val mainDispatcherRule = MainDispatcherRule()
 * ```
 *
 * Exercise 4: Understand how this rule works, then implement FakeBookmarkRepository
 * and write the test methods in HomeViewModelTest.
 *
 * @param dispatcher The test dispatcher to use. Defaults to UnconfinedTestDispatcher
 *                   which executes coroutines eagerly (immediately).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    /**
     * Called before each test. Sets up the test dispatcher.
     */
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    /**
     * Called after each test. Resets the main dispatcher.
     */
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
