package engineer.filip.hoarder.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import engineer.filip.hoarder.data.model.Bookmark

/**
 * Screen-level composable. Handles ViewModel and state management.
 * This is what navigation calls.
 *
 * Events (one-time navigation) are observed via SharedFlow.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
    onNavigateToDeleteConfirmation: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe one-time events for navigation
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeEvent.NavigateToDetail -> onNavigateToDetail(event.bookmarkId)
                is HomeEvent.NavigateToDeleteConfirmation -> onNavigateToDeleteConfirmation(event.bookmarkId)
            }
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.onAction(HomeAction.LoadBookmarks)
        }
    }

    HomeContent(
        state = uiState,
        onAction = viewModel::onAction
    )
}

/**
 * Pure UI composable. See exercise hints for implementation details.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Hoarder") }
                // TODO Exercise 11: Add ClearAll IconButton. See Hints.Exercise11
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO Exercise 12: Create Bookmark and add it. See Hints.Exercise12
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add bookmark"
                )
            }
        }
    ) { innerPadding ->
        // TODO Day 2 Exercise 11: Add loading state. See Hints.Day2Exercise11
        // TODO Day 2 Exercise 12: Add error state with retry. See Hints.Day2Exercise12
        // TODO Day 1 Exercise 9: Add empty state. See Hints.Exercise9
        // TODO Day 2 Exercise 13: Add search TextField. See Hints.Day2Exercise13

        // Day 3 Exercise 4b: Find the performance issue in this LazyColumn
        // Hint: Look at how lambdas are passed to BookmarkItem
        // Stuck? See Hints.Day3Exercise4b
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = state.bookmarks) { bookmark ->
                BookmarkItem(
                    bookmark = bookmark,
                    onClick = { onAction(HomeAction.BookmarkClick(bookmark.id)) },
                    onDeleteClick = { onAction(HomeAction.DeleteBookmarkClick(bookmark.id)) }
                )
            }
        }
    }
}

/**
 * Day 1 Exercise 8: Add border to Card. See Hints.Exercise8
 */
@Composable
fun BookmarkItem(
    bookmark: Bookmark,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO Exercise 8: Add .border(2.dp, Color.Blue) to Card modifier
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookmark.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = bookmark.url,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (bookmark.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bookmark.notes,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete bookmark"
                )
            }
        }
    }
}

/**
 * Day 1 Exercise 9: Build EmptyState composable. See Hints.Exercise9
 */
@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    // TODO: Implement empty state UI (icon, title, subtitle)
}

@Preview(showBackground = true)
@Composable
private fun BookmarkItemPreview() {
    BookmarkItem(
        bookmark = Bookmark(
            id = "1",
            title = "Kotlin Docs",
            url = "https://kotlinlang.org",
            notes = "Great documentation"
        ),
        onClick = {},
        onDeleteClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentPreview() {
    HomeContent(
        state = HomeUiState(
            bookmarks = listOf(
                Bookmark("1", "Google", "https://google.com", "Search engine"),
                Bookmark("2", "Kotlin", "https://kotlinlang.org", ""),
                Bookmark("3", "Android", "https://developer.android.com", "Dev docs")
            )
        ),
        onAction = {},
        modifier = Modifier.padding(WindowInsets.safeDrawing.asPaddingValues())
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentEmptyPreview() {
    HomeContent(
        state = HomeUiState(bookmarks = emptyList()),
        onAction = {},
        modifier = Modifier.padding(WindowInsets.safeDrawing.asPaddingValues())
    )
}
