package engineer.filip.hoarder.data

import engineer.filip.hoarder.ui.Hints
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Day 2 Exercise 6: Implement share intent handling.
 *
 * Create a singleton that holds pending shared content using StateFlow.
 * MainActivity calls onShareReceived(), ViewModel observes pendingShare.
 *
 * Stuck? See Hints.Day2Exercise6
 */
@Singleton
class ShareHandler @Inject constructor() {

    // TODO: Create private MutableStateFlow<String?> and expose as StateFlow
    val pendingShare: StateFlow<String?> = MutableStateFlow(null)

    fun onShareReceived(text: String) {
        // TODO: Set pending share value
    }

    fun consumeShare() {
        // TODO: Clear pending share
    }

    @Suppress("unused")
    private val _hint = Hints.Day2Exercise6
}
