package engineer.filip.hoarder

import engineer.filip.hoarder.data.ShareHandler
import engineer.filip.hoarder.data.model.Bookmark
import engineer.filip.hoarder.ui.home.HomeAction
import engineer.filip.hoarder.ui.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Day 3 Exercise 4: Write HomeViewModel tests.
 *
 * Stuck? See Hints.Day3Exercise4Tests
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeBookmarkRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        repository = FakeBookmarkRepository()
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `initial state has empty bookmarks`() = runTest {
        // TODO: Implement
    }

    @Test
    fun `adding bookmark updates state`() = runTest {
        // TODO: Implement
    }

    @Test
    fun `deleting bookmark removes from state`() = runTest {
        // TODO: Implement
    }

    @Test
    fun `loading bookmarks shows bookmarks`() = runTest {
        // TODO: Implement
    }

    @Test
    fun `error state is set when repository throws`() = runTest {
        // TODO: Implement
    }
}
