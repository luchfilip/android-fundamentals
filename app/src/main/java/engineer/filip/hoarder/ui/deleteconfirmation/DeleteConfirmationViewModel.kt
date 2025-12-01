package engineer.filip.hoarder.ui.deleteconfirmation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import engineer.filip.hoarder.data.model.Bookmark
import engineer.filip.hoarder.data.repository.BookmarkRepository
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
 * Immutable UI state for the Delete Confirmation dialog.
 */
@Immutable
data class DeleteConfirmationUiState(
    val bookmark: Bookmark? = null,
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false
)

/**
 * User actions for the Delete Confirmation dialog.
 */
@Stable
sealed interface DeleteConfirmationAction {
    data object ConfirmDelete : DeleteConfirmationAction
    data object Cancel : DeleteConfirmationAction
}

/**
 * One-time navigation events.
 */
sealed interface DeleteConfirmationEvent {
    data object Dismissed : DeleteConfirmationEvent
    data object DeleteConfirmed : DeleteConfirmationEvent
}

@HiltViewModel
class DeleteConfirmationViewModel @Inject constructor(
    private val repository: BookmarkRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookmarkId: String = checkNotNull(savedStateHandle["bookmarkId"])

    private val _uiState = MutableStateFlow(DeleteConfirmationUiState())
    val uiState: StateFlow<DeleteConfirmationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<DeleteConfirmationEvent>()
    val events: SharedFlow<DeleteConfirmationEvent> = _events.asSharedFlow()

    init {
        loadBookmark()
    }

    fun onAction(action: DeleteConfirmationAction) {
        when (action) {
            is DeleteConfirmationAction.ConfirmDelete -> confirmDelete()
            is DeleteConfirmationAction.Cancel -> cancel()
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
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun confirmDelete() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            try {
                repository.deleteBookmark(bookmarkId)
                _events.emit(DeleteConfirmationEvent.DeleteConfirmed)
            } catch (e: Exception) {
                _uiState.update { it.copy(isDeleting = false) }
            }
        }
    }

    private fun cancel() {
        viewModelScope.launch {
            _events.emit(DeleteConfirmationEvent.Dismissed)
        }
    }
}

