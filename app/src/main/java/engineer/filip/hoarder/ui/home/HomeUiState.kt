package engineer.filip.hoarder.ui.home

import androidx.compose.runtime.Immutable
import engineer.filip.hoarder.data.model.Bookmark
import engineer.filip.hoarder.ui.Hints

/**
 * Immutable UI state for the Home screen.
 * All UI state lives here - single source of truth.
 *
 * Day 1 Exercise 10: Add an `itemCount: Int = 0` field
 * Day 2 Exercise 13: Add searchQuery for debounce search
 *
 * Stuck? Cmd+Click: [engineer.filip.hoarder.ui.Hints.Exercise10] or [engineer.filip.hoarder.ui.Hints.Day2Exercise13]
 */
@Immutable
data class HomeUiState(
    val bookmarks: List<Bookmark> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val counter: Int = 0
    // TODO Day 2 Exercise 13: Add val searchQuery: String = ""
) {
    @Suppress("unused") private val _hint10 = Hints.Exercise10
    @Suppress("unused") private val _hint13 = Hints.Day2Exercise13
}