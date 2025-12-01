package engineer.filip.hoarder.data.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import engineer.filip.hoarder.ui.Hints

/**
 * Data class representing a bookmark.
 *
 * Exercise 3: Add a `createdAt: Long` field with default value `System.currentTimeMillis()`
 * Exercise 4: Add an extension function `isValidUrl()` below this class
 */
data class Bookmark(
    val id: String,
    val title: String,
    val url: String = "",
    val notes: String = "",
    // Exercise 3: Add createdAt field here
    // Stuck? Cmd+Click:
) { private val _hint3 = Hints.Exercise3 }

// Exercise 4: Extension Functions
// TODO: Add an extension function that checks if the URL is valid
// Stuck? Cmd+Click:
private val _hint4 = Hints.Exercise4

/**
 * Day 2 Exercise 10: Extension to parse JSON string into List<Bookmark>
 *
 * Use this in BookmarkRepositoryImpl.getBookmarks() to load from SharedPreferences.
 */
fun String?.toBookmarkList(): List<Bookmark> {
    if (this == null) return emptyList()
    return runCatching {
        val type = object : TypeToken<List<Bookmark>>() {}.type
        val result: List<Bookmark> = Gson().fromJson(this, type)
        result
    }.getOrDefault(emptyList())
}

