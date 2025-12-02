package engineer.filip.hoarder.ui.home

import androidx.compose.runtime.Stable

/**
 * One-time navigation events emitted via SharedFlow.
 * These are consumed once by the UI and trigger navigation.
 */
@Stable
sealed interface HomeEvent {
    data class NavigateToDetail(val bookmarkId: String) : HomeEvent
    data class NavigateToDeleteConfirmation(val bookmarkId: String) : HomeEvent
}