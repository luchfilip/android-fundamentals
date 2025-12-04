package engineer.filip.hoarder.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import engineer.filip.hoarder.data.model.Bookmark
import engineer.filip.hoarder.data.model.toBookmarkList
import engineer.filip.hoarder.ui.Hints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Day 2 Exercise 10: Add persistence using SharedPreferences + Gson.
 *
 * Stuck? See Hints.Day2Exercise10
 */
@Singleton
class BookmarkRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences
) : BookmarkRepository {

    // TODO: Create Gson instance
    private val gson = Gson()

    private val bookmarks = mutableListOf<Bookmark>()

    // TODO: Implement loadFromPrefs()
    // TODO: Implement saveToPrefs()
    private fun saveToPrefs() {
        prefs.edit {
            putString(
                "bookmarks_key", gson.toJson(bookmarks)
            ).apply()
        }
    }

    override suspend fun getBookmarks(): List<Bookmark> = withContext(Dispatchers.IO) {
        val json = prefs.getString("bookmarks_key", null)
        val prefsBookmarks = json.toBookmarkList()
        bookmarks.clear()
        bookmarks.addAll(prefsBookmarks)
        bookmarks.toList()
    }

    override suspend fun getBookmarkById(id: String): Bookmark? = withContext(Dispatchers.IO) {
        bookmarks.find { it.id == id }
    }

    override suspend fun addBookmark(bookmark: Bookmark) = withContext(Dispatchers.IO) {
        bookmarks.add(bookmark)
        saveToPrefs()
    }

    override suspend fun updateBookmark(bookmark: Bookmark) = withContext(Dispatchers.IO) {
        val index = bookmarks.indexOfFirst { it.id == bookmark.id }
        if (index != -1) {
            bookmarks[index] = bookmark
            saveToPrefs()
        }
    }

    override suspend fun deleteBookmark(bookmarkId: String) = withContext(Dispatchers.IO) {
        bookmarks.removeAll { it.id == bookmarkId }
        saveToPrefs()
    }

    override suspend fun clearAll() = withContext(Dispatchers.IO) {
        bookmarks.clear()
        saveToPrefs()
    }

    // TODO: create companion object with private shared prefs key

    // Hint reference
    @Suppress("unused")
    private val _hint = Hints.Day2Exercise10
}
