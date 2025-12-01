package engineer.filip.hoarder.ui.detail

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import engineer.filip.hoarder.data.model.Bookmark
import engineer.filip.hoarder.data.repository.BookmarkRepository
import engineer.filip.hoarder.ui.Hints
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Immutable UI state for the Detail screen.
 */
@Immutable
data class DetailUiState(
    val bookmark: Bookmark? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * User actions/intents for the Detail screen.
 *
 * Exercise 16: Add UpdateNotes action
 * Stuck? Cmd+Click: [Hints.Exercise16]
 */
@Stable
sealed interface DetailAction {
    data object LoadBookmark : DetailAction
    data object NavigateBack : DetailAction
    data object DeleteClick : DetailAction
    // Exercise 16: Add UpdateNotes(val notes: String) action
    // Stuck? Cmd+Click:
    private val _hint16: Any get() = Hints.Exercise16
}

/**
 * One-time navigation events.
 */
sealed interface DetailEvent {
    data object NavigateBack : DetailEvent
    data class NavigateToDeleteConfirmation(val bookmarkId: String) : DetailEvent
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: BookmarkRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookmarkId: String = checkNotNull(savedStateHandle["bookmarkId"])

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DetailEvent>()
    val events: SharedFlow<DetailEvent> = _events.asSharedFlow()

    init {
        loadBookmark()
    }

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.LoadBookmark -> loadBookmark()
            is DetailAction.NavigateBack -> navigateBack()
            is DetailAction.DeleteClick -> onDeleteClick()
            // Exercise 16: Handle UpdateNotes here
        }
    }

    private fun loadBookmark() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val bookmark = repository.getBookmarkById(bookmarkId)
                _uiState.update {
                    it.copy(
                        bookmark = bookmark,
                        isLoading = false,
                        error = if (bookmark == null) "Bookmark not found" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.emit(DetailEvent.NavigateBack)
        }
    }

    private fun onDeleteClick() {
        viewModelScope.launch {
            _events.emit(DetailEvent.NavigateToDeleteConfirmation(bookmarkId))
        }
    }

    // Exercise 16: Add updateNotes function
    // private fun updateNotes(notes: String) {
    //     viewModelScope.launch {
    //         val current = _uiState.value.bookmark ?: return@launch
    //         val updated = current.copy(notes = notes)
    //         repository.updateBookmark(updated)
    //         _uiState.update { it.copy(bookmark = updated) }
    //     }
    // }
}
