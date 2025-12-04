package engineer.filip.hoarder.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

import javax.inject.Singleton


@Singleton
class ShareHandler @Inject constructor() {

    private val _pendingShare = MutableStateFlow<String?>(null)
    val pendingShare: StateFlow<String?> = _pendingShare.asStateFlow()

    private val _deeplinkData = MutableStateFlow<String?>(null)
    val deeplinkData: StateFlow<String?> = _deeplinkData.asStateFlow()

    fun onShareReceived(text: String?) {
        text?.let { input ->
            _pendingShare.update { input }
        }
    }

    fun consumeIntent() {
        _pendingShare.update { null }
    }

    fun onDeeplinkReceived(id: String?) {
        id?.let { bookmarkId ->
            _deeplinkData.update { bookmarkId }
        }
    }

    fun consumeDeeplink() {
        _deeplinkData.update { null }
    }
}