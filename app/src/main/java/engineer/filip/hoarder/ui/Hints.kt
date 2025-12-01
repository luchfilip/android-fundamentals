package engineer.filip.hoarder.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import engineer.filip.hoarder.data.model.Bookmark
import java.util.UUID

/**
 * Hints for exercises - Cmd+Click to navigate here if you're stuck!
 *
 * Don't peek unless you need to! Try to figure it out first.
 *
 * Organization:
 * - Day 1: Exercises 1-12 (Kotlin, Compose, MVI basics)
 * - Day 2: Exercises 5-13 (Intents, Persistence, States, Search)
 * - Day 3: Exercise 4 (Testing)
 */
@Suppress("unused")
object Hints {

    // ========================================
    // DAY 1 - KOTLIN EXERCISES
    // ========================================

    /**
     * Exercise 3: Add createdAt field to Bookmark
     *
     * Solution - add to the Bookmark data class:
     * ```kotlin
     * data class Bookmark(
     *     val id: String,
     *     val title: String,
     *     val url: String = "",
     *     val notes: String = "",
     *     val createdAt: Long = System.currentTimeMillis()  // Add this
     * )
     * ```
     */
    object Exercise3

    /**
     * Exercise 4: Add isValidUrl() extension function
     *
     * Solution - add below the Bookmark class:
     * ```kotlin
     * fun Bookmark.isValidUrl(): Boolean {
     *     return url.startsWith("http://") || url.startsWith("https://")
     * }
     * ```
     *
     * Alternative with Patterns:
     * ```kotlin
     * fun Bookmark.isValidUrl(): Boolean {
     *     return android.util.Patterns.WEB_URL.matcher(url).matches()
     * }
     * ```
     */
    object Exercise4

    // ========================================
    // DAY 1 - COMPOSE EXERCISES
    // ========================================

    /**
     * Exercise 6: Modify the UI
     *
     * Find the TopAppBar and change the title text.
     *
     * Solution:
     * ```kotlin
     * TopAppBar(
     *     title = { Text("My Hoarder") },  // Changed from "Hoarder"
     * )
     * ```
     */
    object Exercise6

    /**
     * Exercise 7: Add a Counter
     *
     * Solution - add inside HomeContent, before or after Scaffold:
     * ```kotlin
     * var counter by remember { mutableStateOf(0) }
     * ```
     *
     * Then in FloatingActionButton onClick:
     * ```kotlin
     * onClick = { counter++ }
     * ```
     *
     * And replace Icon with:
     * ```kotlin
     * Text("$counter")
     * ```
     */
    object Exercise7

    /**
     * Exercise 8: Style BookmarkItem
     *
     * Add border to the Card modifier:
     * ```kotlin
     * Card(
     *     onClick = onClick,
     *     modifier = modifier
     *         .fillMaxWidth()
     *         .border(2.dp, Color.Blue)  // Add this
     * )
     * ```
     *
     * Move border BEFORE fillMaxWidth to see the difference:
     * ```kotlin
     * modifier = modifier
     *     .border(2.dp, Color.Blue)  // Border first
     *     .fillMaxWidth()
     * ```
     */
    object Exercise8

    /**
     * Exercise 9: Build EmptyState Composable
     *
     * Full solution - add at the bottom of HomeScreen.kt:
     */
    object Exercise9 {
        @Composable
        fun EmptyState(
            onAddClick: () -> Unit = {},
            modifier: Modifier = Modifier
        ) {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No bookmarks yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onAddClick) {
                    Text("Add your first bookmark")
                }
            }
        }

        /**
         * Then in HomeContent, wrap LazyColumn with a condition:
         * ```kotlin
         * if (state.bookmarks.isEmpty()) {
         *     EmptyState(
         *         onAddClick = { /* trigger add */ }
         *     )
         * } else {
         *     LazyColumn(...) { ... }
         * }
         * ```
         */
        const val USAGE_HINT = "See above"
    }

    /**
     * Exercise 10: Add itemCount to State
     *
     * Step 1 - In HomeUiState, add the field:
     * ```kotlin
     * @Immutable
     * data class HomeUiState(
     *     val bookmarks: List<Bookmark> = emptyList(),
     *     val isLoading: Boolean = false,
     *     val error: String? = null,
     *     val itemCount: Int = 0  // Add this
     * )
     * ```
     *
     * Step 2 - In loadBookmarks(), update the state:
     * ```kotlin
     * _uiState.update {
     *     it.copy(
     *         bookmarks = bookmarks,
     *         itemCount = bookmarks.size,  // Add this
     *         isLoading = false,
     *         error = null
     *     )
     * }
     * ```
     *
     * Step 3 - In HomeScreen TopAppBar:
     * ```kotlin
     * TopAppBar(
     *     title = { Text("Hoarder (${state.itemCount})") },
     * )
     * ```
     */
    object Exercise10

    /**
     * Exercise 11: Add ClearAll Action
     *
     * Step 1 - Add to HomeAction:
     * ```kotlin
     * sealed interface HomeAction {
     *     data object LoadBookmarks : HomeAction
     *     data class AddBookmark(val bookmark: Bookmark) : HomeAction
     *     data class DeleteBookmark(val bookmarkId: String) : HomeAction
     *     data class DeleteBookmarkClick(val bookmarkId: String) : HomeAction
     *     data class BookmarkClick(val bookmarkId: String) : HomeAction
     *     data object ClearAll : HomeAction  // Add this
     * }
     * ```
     *
     * Step 2 - Handle in onAction():
     * ```kotlin
     * fun onAction(action: HomeAction) {
     *     when (action) {
     *         // ... existing cases ...
     *         is HomeAction.ClearAll -> clearAll()
     *     }
     * }
     * ```
     *
     * Step 3 - Implement clearAll():
     * ```kotlin
     * private fun clearAll() {
     *     viewModelScope.launch {
     *         repository.clearAll()
     *         loadBookmarks()
     *     }
     * }
     * ```
     *
     * Step 4 - Add IconButton in TopAppBar:
     * ```kotlin
     * TopAppBar(
     *     title = { Text("Hoarder") },
     *     actions = {
     *         IconButton(onClick = { onAction(HomeAction.ClearAll) }) {
     *             Icon(
     *                 imageVector = Icons.Default.Clear,
     *                 contentDescription = "Clear all"
     *             )
     *         }
     *     }
     * )
     * ```
     */
    object Exercise11

    /**
     * Exercise 12: Complete Add Functionality
     *
     * Full solution for FAB onClick:
     * ```kotlin
     * FloatingActionButton(
     *     onClick = {
     *         val bookmark = Bookmark(
     *             id = UUID.randomUUID().toString(),
     *             title = "Bookmark ${System.currentTimeMillis() % 100}",
     *             url = "https://example.com/${System.currentTimeMillis()}",
     *             notes = "Added via FAB"
     *         )
     *         onAction(HomeAction.AddBookmark(bookmark))
     *     }
     * )
     * ```
     */
    object Exercise12 {
        fun createBookmark(): Bookmark {
            return Bookmark(
                id = UUID.randomUUID().toString(),
                title = "Bookmark ${System.currentTimeMillis() % 100}",
                url = "https://example.com/${System.currentTimeMillis()}",
                notes = "Added via FAB"
            )
        }
    }

    /**
     * Exercise 12b: Add Item Number to Bookmark Title
     *
     * Use state.bookmarks.size to show item number:
     * ```kotlin
     * FloatingActionButton(
     *     onClick = {
     *         val bookmark = Bookmark(
     *             id = UUID.randomUUID().toString(),
     *             title = "Bookmark #${state.bookmarks.size + 1}",
     *             url = "https://example.com/${System.currentTimeMillis()}",
     *             notes = "Added via FAB"
     *         )
     *         onAction(HomeAction.AddBookmark(bookmark))
     *     }
     * )
     * ```
     */
    object Exercise12b {
        fun createTitleWithNumber(currentSize: Int): String {
            return "Bookmark #${currentSize + 1}"
        }
    }

    /**
     * Exercise 16: Add Edit Notes to Detail Screen
     *
     * Step 1 - Add UpdateNotes to DetailAction:
     * ```kotlin
     * sealed interface DetailAction {
     *     data object LoadBookmark : DetailAction
     *     data object NavigateBack : DetailAction
     *     data object DeleteClick : DetailAction
     *     data class UpdateNotes(val notes: String) : DetailAction  // Add this
     * }
     * ```
     *
     * Step 2 - Handle in DetailViewModel.onAction():
     * ```kotlin
     * fun onAction(action: DetailAction) {
     *     when (action) {
     *         // ... existing cases ...
     *         is DetailAction.UpdateNotes -> updateNotes(action.notes)
     *     }
     * }
     * ```
     *
     * Step 3 - Implement updateNotes():
     * ```kotlin
     * private fun updateNotes(notes: String) {
     *     viewModelScope.launch {
     *         val current = _uiState.value.bookmark ?: return@launch
     *         val updated = current.copy(notes = notes)
     *         repository.updateBookmark(updated)
     *         _uiState.update { it.copy(bookmark = updated) }
     *     }
     * }
     * ```
     *
     * Step 4 - Uncomment the TextField in DetailScreen.kt:
     * ```kotlin
     * Spacer(modifier = Modifier.height(16.dp))
     * var editedNotes by remember { mutableStateOf(bookmark.notes) }
     * OutlinedTextField(
     *     value = editedNotes,
     *     onValueChange = { editedNotes = it },
     *     label = { Text("Edit Notes") },
     *     modifier = Modifier.fillMaxWidth()
     * )
     * Spacer(modifier = Modifier.height(8.dp))
     * Button(onClick = { onAction(DetailAction.UpdateNotes(editedNotes)) }) {
     *     Text("Save")
     * }
     * ```
     */
    object Exercise16

    // ========================================
    // DAY 2 - MANIFEST & INTENTS EXERCISES
    // ========================================

    /**
     * Day 2 Exercise 1: Add Internet Permission
     *
     * Add before <application> in AndroidManifest.xml:
     * ```xml
     * <uses-permission android:name="android.permission.INTERNET" />
     * ```
     */
    object Day2Exercise1

    /**
     * Day 2 Exercise 2: Log App Start
     *
     * In HoarderApp.onCreate():
     * ```kotlin
     * Log.d("Hoarder", "Application started!")
     * ```
     *
     * Required import: android.util.Log
     */
    object Day2Exercise2

    /**
     * Day 2 Exercise 3: Make URL Clickable
     *
     * In DetailScreen, make the URL Text clickable:
     * ```kotlin
     * val context = LocalContext.current
     *
     * Text(
     *     text = bookmark.url,
     *     style = MaterialTheme.typography.bodyLarge,
     *     color = MaterialTheme.colorScheme.primary,
     *     modifier = Modifier.clickable {
     *         val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.url))
     *         context.startActivity(intent)
     *     }
     * )
     * ```
     *
     * Required imports:
     * - android.content.Intent
     * - android.net.Uri
     * - androidx.compose.ui.platform.LocalContext
     * - androidx.compose.foundation.clickable
     */
    object Day2Exercise3

    /**
     * Day 2 Exercise 5: Add Share Intent Filter to AndroidManifest.xml
     *
     * Add this inside your <activity> tag, after the launcher intent filter:
     * ```xml
     * <intent-filter>
     *     <action android:name="android.intent.action.SEND" />
     *     <category android:name="android.intent.category.DEFAULT" />
     *     <data android:mimeType="text/plain" />
     * </intent-filter>
     * ```
     *
     * Full activity example:
     * ```xml
     * <activity
     *     android:name=".MainActivity"
     *     android:exported="true">
     *     <!-- Launcher filter -->
     *     <intent-filter>
     *         <action android:name="android.intent.action.MAIN" />
     *         <category android:name="android.intent.category.LAUNCHER" />
     *     </intent-filter>
     *     <!-- Share filter -->
     *     <intent-filter>
     *         <action android:name="android.intent.action.SEND" />
     *         <category android:name="android.intent.category.DEFAULT" />
     *         <data android:mimeType="text/plain" />
     *     </intent-filter>
     * </activity>
     * ```
     */
    object Day2Exercise5

    /**
     * Day 2 Exercise 6: Create ShareHandler singleton
     *
     * Create file: data/ShareHandler.kt
     *
     * ```kotlin
     * @Singleton
     * class ShareHandler @Inject constructor() {
     *
     *     private val _pendingShare = MutableStateFlow<String?>(null)
     *     val pendingShare: StateFlow<String?> = _pendingShare.asStateFlow()
     *
     *     fun onShareReceived(text: String) {
     *         _pendingShare.value = text
     *     }
     *
     *     fun consumeShare() {
     *         _pendingShare.value = null
     *     }
     * }
     * ```
     *
     * Required imports:
     * - kotlinx.coroutines.flow.MutableStateFlow
     * - kotlinx.coroutines.flow.StateFlow
     * - kotlinx.coroutines.flow.asStateFlow
     * - javax.inject.Inject
     * - javax.inject.Singleton
     */
    object Day2Exercise6

    /**
     * Day 2 Exercise 7: Handle Share Intent in MainActivity
     *
     * Step 1 - Inject ShareHandler:
     * ```kotlin
     * @Inject
     * lateinit var shareHandler: ShareHandler
     * ```
     *
     * Step 2 - Call handleShareIntent in onCreate():
     * ```kotlin
     * override fun onCreate(savedInstanceState: Bundle?) {
     *     super.onCreate(savedInstanceState)
     *     handleShareIntent(intent)  // Add this
     *     // ... rest of onCreate
     * }
     * ```
     *
     * Step 3 - Override onNewIntent:
     * ```kotlin
     * override fun onNewIntent(intent: Intent) {
     *     super.onNewIntent(intent)
     *     handleShareIntent(intent)
     * }
     * ```
     *
     * Step 4 - Implement handleShareIntent:
     * ```kotlin
     * private fun handleShareIntent(intent: Intent?) {
     *     if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
     *         val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
     *         sharedText?.let { shareHandler.onShareReceived(it) }
     *     }
     * }
     * ```
     */
    object Day2Exercise7

    /**
     * Day 2 Exercise 8: Observe ShareHandler in HomeViewModel
     *
     * Step 1 - Add ShareHandler to constructor:
     * ```kotlin
     * @HiltViewModel
     * class HomeViewModel @Inject constructor(
     *     private val repository: BookmarkRepository,
     *     private val shareHandler: ShareHandler  // Add this
     * ) : ViewModel()
     * ```
     *
     * Step 2 - Call observeSharedContent() in init:
     * ```kotlin
     * init {
     *     loadBookmarks()
     *     observeSharedContent()  // Add this
     * }
     * ```
     *
     * Step 3 - Implement observeSharedContent():
     * ```kotlin
     * private fun observeSharedContent() {
     *     viewModelScope.launch {
     *         shareHandler.pendingShare.collect { sharedText ->
     *             sharedText?.let { text ->
     *                 val newBookmark = Bookmark(
     *                     id = UUID.randomUUID().toString(),
     *                     title = extractTitle(text),
     *                     url = text
     *                 )
     *                 repository.addBookmark(newBookmark)
     *                 shareHandler.consumeShare()
     *                 loadBookmarks()
     *             }
     *         }
     *     }
     * }
     * ```
     */
    object Day2Exercise8

    /**
     * Day 2 Exercise 9: Improve Title Extraction
     *
     * Better implementation of extractTitle():
     * ```kotlin
     * private fun extractTitle(text: String): String {
     *     // Check if it's a URL
     *     if (text.startsWith("http://") || text.startsWith("https://")) {
     *         return try {
     *             Uri.parse(text).host ?: "Shared Link"
     *         } catch (e: Exception) {
     *             "Shared Link"
     *         }
     *     }
     *     // For plain text, use first 50 chars as title
     *     return text.take(50).let { if (text.length > 50) "$it..." else it }
     * }
     * ```
     *
     * Required import: android.net.Uri
     */
    object Day2Exercise9

    /**
     * Day 2 Exercise 10: Add SharedPreferences Persistence
     *
     * Step 1: Add SharedPreferences provider in DataModule:
     * ```kotlin
     * @Module
     * @InstallIn(SingletonComponent::class)
     * object DataModule {
     *     @Provides
     *     @Singleton
     *     fun provideSharedPreferences(
     *         @ApplicationContext context: Context
     *     ): SharedPreferences {
     *         return context.getSharedPreferences("hoarder_bookmarks", Context.MODE_PRIVATE)
     *     }
     * }
     * ```
     *
     * Step 2: Update BookmarkRepositoryImpl:
     * ```kotlin
     * @Singleton
     * class BookmarkRepositoryImpl @Inject constructor(
     *     private val prefs: SharedPreferences
     * ) : BookmarkRepository {
     *
     *     private val bookmarks = mutableListOf<Bookmark>()
     *
     *     override suspend fun getBookmarks(): List<Bookmark> = withContext(Dispatchers.IO) {
     *         val loaded = prefs.getString(BOOKMARKS_KEY, null).toBookmarkList()
     *         bookmarks.clear()
     *         bookmarks.addAll(loaded)
     *         bookmarks.toList()
     *     }
     *
     *     override suspend fun addBookmark(bookmark: Bookmark) = withContext(Dispatchers.IO) {
     *         bookmarks.add(bookmark)
     *         saveToPrefs()
     *     }
     *
     *     override suspend fun deleteBookmark(id: String) = withContext(Dispatchers.IO) {
     *         bookmarks.removeAll { it.id == id }
     *         saveToPrefs()
     *     }
     *
     *     private fun saveToPrefs() {
     *         prefs.edit().putString(BOOKMARKS_KEY, Gson().toJson(bookmarks)).apply()
     *     }
     *
     *     companion object {
     *         private const val BOOKMARKS_KEY = "bookmarks"
     *     }
     * }
     * ```
     *
     * Note: toBookmarkList() extension is already defined in Bookmark.kt
     *
     * Required imports for DataModule:
     * - android.content.Context
     * - android.content.SharedPreferences
     * - dagger.Provides
     * - dagger.hilt.android.qualifiers.ApplicationContext
     * - javax.inject.Singleton
     *
     * Required imports for BookmarkRepositoryImpl:
     * - android.content.SharedPreferences
     * - com.google.gson.Gson
     * - kotlinx.coroutines.Dispatchers
     * - kotlinx.coroutines.withContext
     */
    object Day2Exercise10

    /**
     * Day 2 Exercise 11: Add Loading State UI
     *
     * In HomeContent, use a when block to handle states:
     * ```kotlin
     * when {
     *     state.isLoading -> {
     *         Box(
     *             modifier = Modifier.fillMaxSize().padding(innerPadding),
     *             contentAlignment = Alignment.Center
     *         ) {
     *             CircularProgressIndicator()
     *         }
     *     }
     *     state.error != null -> { /* Exercise 12 */ }
     *     state.bookmarks.isEmpty() -> { EmptyState() }
     *     else -> { /* Normal list */ }
     * }
     * ```
     */
    object Day2Exercise11

    /**
     * Day 2 Exercise 12: Add Error State with Retry
     *
     * In the when block, handle error state:
     * ```kotlin
     * state.error != null -> {
     *     Column(
     *         modifier = Modifier.fillMaxSize().padding(innerPadding),
     *         verticalArrangement = Arrangement.Center,
     *         horizontalAlignment = Alignment.CenterHorizontally
     *     ) {
     *         Icon(
     *             Icons.Default.Warning,
     *             contentDescription = null,
     *             tint = MaterialTheme.colorScheme.error,
     *             modifier = Modifier.size(48.dp)
     *         )
     *         Spacer(modifier = Modifier.height(16.dp))
     *         Text("Something went wrong", style = MaterialTheme.typography.titleMedium)
     *         Text(state.error ?: "", style = MaterialTheme.typography.bodySmall)
     *         Spacer(modifier = Modifier.height(16.dp))
     *         Button(onClick = { onAction(HomeAction.LoadBookmarks) }) {
     *             Text("Retry")
     *         }
     *     }
     * }
     * ```
     */
    object Day2Exercise12

    /**
     * Day 2 Exercise 14: Polish Detail Screen
     *
     * 1. Show formatted created date:
     * ```kotlin
     * val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
     * Text("Created: ${dateFormat.format(Date(bookmark.createdAt))}")
     * ```
     *
     * 2. Add Copy URL button:
     * ```kotlin
     * val context = LocalContext.current
     * val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
     *
     * Button(onClick = {
     *     val clip = ClipData.newPlainText("URL", bookmark.url)
     *     clipboardManager.setPrimaryClip(clip)
     * }) {
     *     Text("Copy URL")
     * }
     * ```
     */
    object Day2Exercise14

    /**
     * Day 2 Exercise 13: Add Search with Debounce
     *
     * Step 1 - Add searchQuery to HomeUiState:
     * ```kotlin
     * data class HomeUiState(
     *     val bookmarks: List<Bookmark> = emptyList(),
     *     val isLoading: Boolean = false,
     *     val error: String? = null,
     *     val searchQuery: String = ""  // Add this
     * )
     * ```
     *
     * Step 2 - Add SearchQueryChanged action:
     * ```kotlin
     * sealed interface HomeAction {
     *     // ... existing actions ...
     *     data class SearchQueryChanged(val query: String) : HomeAction
     * }
     * ```
     *
     * Step 3 - In HomeViewModel, add search flow:
     * ```kotlin
     * private val searchQuery = MutableStateFlow("")
     * private var allBookmarks: List<Bookmark> = emptyList()
     *
     * init {
     *     loadBookmarks()
     *     observeSharedContent()
     *     observeSearch()  // Add this
     * }
     *
     * private fun observeSearch() {
     *     viewModelScope.launch {
     *         searchQuery
     *             .debounce(300)
     *             .collectLatest { query ->
     *                 val filtered = if (query.isEmpty()) allBookmarks
     *                     else allBookmarks.filter {
     *                         it.title.contains(query, ignoreCase = true) ||
     *                         it.url.contains(query, ignoreCase = true)
     *                     }
     *                 _uiState.update { it.copy(bookmarks = filtered, searchQuery = query) }
     *             }
     *     }
     * }
     *
     * private fun onSearchQueryChanged(query: String) {
     *     searchQuery.value = query
     * }
     * ```
     *
     * Step 4 - Add TextField in HomeScreen:
     * ```kotlin
     * OutlinedTextField(
     *     value = state.searchQuery,
     *     onValueChange = { onAction(HomeAction.SearchQueryChanged(it)) },
     *     placeholder = { Text("Search bookmarks...") },
     *     leadingIcon = { Icon(Icons.Default.Search, null) },
     *     trailingIcon = {
     *         if (state.searchQuery.isNotEmpty()) {
     *             IconButton(onClick = { onAction(HomeAction.SearchQueryChanged("")) }) {
     *                 Icon(Icons.Default.Clear, "Clear")
     *             }
     *         }
     *     },
     *     modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
     * )
     * ```
     *
     * Required imports:
     * - kotlinx.coroutines.FlowPreview
     * - kotlinx.coroutines.flow.debounce
     * - kotlinx.coroutines.flow.collectLatest
     */
    object Day2Exercise13

    // ========================================
    // DAY 3 - TESTING EXERCISES
    // ========================================

    /**
     * Day 3 Exercise 4: Create MainDispatcherRule
     *
     * Full solution for MainDispatcherRule.kt:
     * ```kotlin
     * @OptIn(ExperimentalCoroutinesApi::class)
     * class MainDispatcherRule(
     *     private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
     * ) : TestWatcher() {
     *
     *     override fun starting(description: Description) {
     *         Dispatchers.setMain(dispatcher)
     *     }
     *
     *     override fun finished(description: Description) {
     *         Dispatchers.resetMain()
     *     }
     * }
     * ```
     *
     * Required imports:
     * - kotlinx.coroutines.Dispatchers
     * - kotlinx.coroutines.ExperimentalCoroutinesApi
     * - kotlinx.coroutines.test.TestDispatcher
     * - kotlinx.coroutines.test.UnconfinedTestDispatcher
     * - kotlinx.coroutines.test.resetMain
     * - kotlinx.coroutines.test.setMain
     * - org.junit.rules.TestWatcher
     * - org.junit.runner.Description
     */
    object Day3Exercise4

    /**
     * Day 3 Exercise 4: Create FakeBookmarkRepository
     *
     * Full solution for FakeBookmarkRepository.kt:
     * ```kotlin
     * class FakeBookmarkRepository : BookmarkRepository {
     *     private val bookmarks = mutableListOf<Bookmark>()
     *     var shouldThrowError = false
     *
     *     override suspend fun getBookmarks(): List<Bookmark> {
     *         if (shouldThrowError) throw RuntimeException("Test error")
     *         return bookmarks.toList()
     *     }
     *
     *     override suspend fun getBookmarkById(id: String) = bookmarks.find { it.id == id }
     *
     *     override suspend fun addBookmark(bookmark: Bookmark) {
     *         bookmarks.add(bookmark)
     *     }
     *
     *     override suspend fun updateBookmark(bookmark: Bookmark) {
     *         val index = bookmarks.indexOfFirst { it.id == bookmark.id }
     *         if (index != -1) bookmarks[index] = bookmark
     *     }
     *
     *     override suspend fun deleteBookmark(bookmarkId: String) {
     *         bookmarks.removeAll { it.id == bookmarkId }
     *     }
     *
     *     override suspend fun clearAll() {
     *         bookmarks.clear()
     *     }
     *
     *     fun getBookmarkCount() = bookmarks.size
     * }
     * ```
     */
    object Day3Exercise4Fake

    /**
     * Day 3 Exercise 4: HomeViewModel Test Examples
     *
     * Test setup:
     * ```kotlin
     * @OptIn(ExperimentalCoroutinesApi::class)
     * class HomeViewModelTest {
     *
     *     @get:Rule
     *     val mainDispatcherRule = MainDispatcherRule()
     *
     *     private lateinit var repository: FakeBookmarkRepository
     *     private lateinit var viewModel: HomeViewModel
     *
     *     @Before
     *     fun setup() {
     *         repository = FakeBookmarkRepository()
     *         viewModel = HomeViewModel(repository)
     *     }
     * }
     * ```
     *
     * Example test - Initial state:
     * ```kotlin
     * @Test
     * fun `initial state has empty bookmarks`() = runTest {
     *     advanceUntilIdle()
     *     val state = viewModel.uiState.value
     *     assertTrue(state.bookmarks.isEmpty())
     *     assertFalse(state.isLoading)
     *     assertNull(state.error)
     * }
     * ```
     *
     * Example test - Adding bookmark:
     * ```kotlin
     * @Test
     * fun `adding bookmark updates state`() = runTest {
     *     val bookmark = Bookmark(id = "1", title = "Test", url = "https://example.com")
     *     viewModel.onAction(HomeAction.AddBookmark(bookmark))
     *     advanceUntilIdle()
     *     assertEquals(1, viewModel.uiState.value.bookmarks.size)
     * }
     * ```
     *
     * Example test - Error state:
     * ```kotlin
     * @Test
     * fun `error state is set when repository throws`() = runTest {
     *     repository.shouldThrowError = true
     *     viewModel.onAction(HomeAction.LoadBookmarks)
     *     advanceUntilIdle()
     *     assertEquals("Test error", viewModel.uiState.value.error)
     * }
     * ```
     */
    object Day3Exercise4Tests

    /**
     * Day 3 Exercise 4b: Find the Recomposition Issue
     *
     * Location: HomeScreen.kt, lines 121-126 (LazyColumn items block)
     *
     * The problem:
     * ```kotlin
     * items(items = state.bookmarks) { bookmark ->
     *     BookmarkItem(
     *         bookmark = bookmark,
     *         onClick = { onAction(HomeAction.BookmarkClick(bookmark.id)) },
     *         onDeleteClick = { onAction(HomeAction.DeleteBookmarkClick(bookmark.id)) }
     *     )
     * }
     * ```
     *
     * Issue: New lambdas are created for onClick and onDeleteClick on every
     * recomposition for each item. This creates garbage and can cause
     * unnecessary recompositions.
     *
     * The fix:
     * ```kotlin
     * items(items = state.bookmarks, key = { it.id }) { bookmark ->
     *     BookmarkItem(
     *         bookmark = bookmark,
     *         onClick = remember(bookmark.id) {
     *             { onAction(HomeAction.BookmarkClick(bookmark.id)) }
     *         },
     *         onDeleteClick = remember(bookmark.id) {
     *             { onAction(HomeAction.DeleteBookmarkClick(bookmark.id)) }
     *         }
     *     )
     * }
     * ```
     *
     * Key improvements:
     * 1. Added `key = { it.id }` - helps Compose track items efficiently
     * 2. Used `remember(bookmark.id)` - caches lambdas, only recreates when id changes
     *
     * Note: For small lists this is minor, but for large lists (100+ items)
     * this optimization matters significantly!
     */
    object Day3Exercise4b
}
