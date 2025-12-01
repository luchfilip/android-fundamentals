package engineer.filip.hoarder.data.repository

import engineer.filip.hoarder.data.model.Bookmark
import engineer.filip.hoarder.ui.Hints
import kotlinx.coroutines.Dispatchers
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
    // TODO: Inject SharedPreferences here
) : BookmarkRepository {

    // TODO: Create Gson instance

    private val bookmarks = mutableListOf<Bookmark>()

    // TODO: Implement loadFromPrefs()
    // TODO: Implement saveToPrefs()

    override suspend fun getBookmarks(): List<Bookmark> = withContext(Dispatchers.IO) {
        bookmarks.toList()
    }

    override suspend fun getBookmarkById(id: String): Bookmark? = withContext(Dispatchers.IO) {
        bookmarks.find { it.id == id }
    }

    override suspend fun addBookmark(bookmark: Bookmark) = withContext(Dispatchers.IO) {
        bookmarks.add(bookmark)
        // TODO: Call saveToPrefs()
        Unit
    }

    override suspend fun updateBookmark(bookmark: Bookmark) = withContext(Dispatchers.IO) {
        val index = bookmarks.indexOfFirst { it.id == bookmark.id }
        if (index != -1) {
            bookmarks[index] = bookmark
            // TODO: Call saveToPrefs()
        }
    }

    override suspend fun deleteBookmark(bookmarkId: String) = withContext(Dispatchers.IO) {
        bookmarks.removeAll { it.id == bookmarkId }
        // TODO: Call saveToPrefs()
        Unit
    }

    override suspend fun clearAll() = withContext(Dispatchers.IO) {
        bookmarks.clear()
        // TODO: Call saveToPrefs()
    }

    // TODO: create companion object with private shared prefs key

    // Hint reference
    @Suppress("unused")
    private val _hint = Hints.Day2Exercise10
}
