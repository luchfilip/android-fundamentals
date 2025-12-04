package engineer.filip.hoarder

import engineer.filip.hoarder.data.model.Bookmark
import engineer.filip.hoarder.data.repository.BookmarkRepository

/**
 * Day 3 Exercise 4: Implement FakeBookmarkRepository for testing.
 *
 * Stuck? See Hints.Day3Exercise4Fake
 */
class FakeBookmarkRepository : BookmarkRepository {

    private val bookmarks = mutableListOf<Bookmark>()

    // TODO: Create in-memory storage
    var shouldThrowError = false

    override suspend fun getBookmarks(): List<Bookmark> {
        if (shouldThrowError) throw Exception("Unknown error")
        return bookmarks
    }

    override suspend fun getBookmarkById(id: String): Bookmark? {
        return bookmarks.find { it.id == id }
    }

    override suspend fun addBookmark(bookmark: Bookmark) {
        bookmarks.add(bookmark)
    }

    override suspend fun updateBookmark(bookmark: Bookmark) {
        val index = bookmarks.indexOfFirst { it.id == bookmark.id }
        bookmarks[index] = bookmark
    }

    override suspend fun deleteBookmark(bookmarkId: String) {
        bookmarks.removeAll { it.id == bookmarkId }
    }

    override suspend fun clearAll() {
        bookmarks.clear()
    }

    fun getBookmarkCount(): Int = bookmarks.size
}
