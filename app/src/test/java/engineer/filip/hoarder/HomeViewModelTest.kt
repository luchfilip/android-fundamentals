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
    private lateinit var shareHandler: ShareHandler
    @Before
    fun setup() {
        repository = FakeBookmarkRepository()
        shareHandler = ShareHandler()
        viewModel = HomeViewModel(repository, shareHandler)
    }

    @Test
    fun `initial state has empty bookmarks`() = runTest {
        val state = viewModel.uiState.value
        assertTrue(state.bookmarks.isEmpty())
    }

    @Test
    fun `adding bookmark updates state`() = runTest {
        viewModel.onAction(HomeAction.LoadBookmarks)
        val bookmark = Bookmark(
            id = "1",
            title = "Title",
            url = "https://google.com"
        )
        viewModel.onAction(HomeAction.AddBookmark(bookmark))
        assertEquals(1, viewModel.uiState.value.bookmarks.size)
    }

    @Test
    fun `deleting bookmark removes from state`() = runTest {
        viewModel.onAction(HomeAction.LoadBookmarks)
        val bookmark = Bookmark(
            id = "X",
            title = "title",
            url = "url"
        )
        viewModel.onAction(HomeAction.AddBookmark(bookmark))
        advanceUntilIdle()
        assert(viewModel.uiState.value.bookmarks.find { it.id == "X" } != null)
        viewModel.onAction(HomeAction.DeleteBookmark(bookmark.id))
        advanceUntilIdle()
        assert(viewModel.uiState.value.bookmarks.find { it.id == "X" } == null)
    }

    @Test
    fun `loading bookmarks shows bookmarks`() = runTest {
        // TODO: Implement
    }

    @Test
    fun `error state is set when repository throws`() = runTest {
        repository.shouldThrowError = true

        viewModel.onAction(HomeAction.LoadBookmarks)
        advanceUntilIdle()
        assertEquals("Unknown error", viewModel.uiState.value.error)
    }
}
