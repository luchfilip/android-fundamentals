package engineer.filip.hoarder.ui.home

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import engineer.filip.hoarder.data.model.Bookmark
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.jar.Manifest

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

    val locationPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        when {
            fineLocationGranted && coarseLocationGranted -> {
                viewModel.onAction(HomeAction.OnFineLocationPermissionUpdated(true))
            }
            coarseLocationGranted -> {
                viewModel.onAction(HomeAction.OnCoarseLocationPermissionUpdated(true))
            }
            else -> {

            }
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    }

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
        onAction = viewModel::onAction,
        onRequestPermissions = {
            locationPermissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
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
    modifier: Modifier = Modifier,
    onRequestPermissions: () -> Unit
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Hoarder") },
                // TODO Exercise 11: Add ClearAll IconButton. See Hints.Exercise11
                actions = {
                    val icon = when {
                        state.hasLocationCoarsePermission -> Icons.Default.LocationOn
                        state.hasLocationFinePermission -> Icons.Default.AddLocationAlt
                        else -> {
                           Icons.Default.LocationOff
                        }
                    }
                    Icon(imageVector = icon,
                        contentDescription = "location icon",
                        modifier = Modifier.clickable {
                            Log.d("HomeScreen", "icon clicked")
                            onRequestPermissions()
                        }.padding(8.dp))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete all",
                        modifier = Modifier.clickable {
                            onAction(HomeAction.ClearAll)
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(
                        HomeAction.AddBookmark(
                            Bookmark(
                                id = UUID.randomUUID().toString(),
                                title = "Bookmark Title",
                                url = "https://google.com",
                                notes = "My custom note"
                            )
                        )
                    )
                }
            ) {
                Text(
                    text = "#${state.counter}",
                    fontSize = 16.sp
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                searchQuery = state.searchQuery,
                onAction = onAction
            )
            when {
                state.bookmarks.isEmpty() -> {
                    EmptyState(modifier = Modifier)
                }

                else -> {
                    val bookmarks =
                        if (state.searchQuery.isEmpty()) state.bookmarks else state.filteredBookmarks
                    BookmarkItems(
                        modifier = Modifier,
                        bookmarks = bookmarks,
                        onAction = onAction
                    )
                }
            }
        }
    }
}

@Composable
fun SearchInput(
    modifier: Modifier,
    searchQuery: String = "",
    onAction: (HomeAction) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = searchQuery,
        label = { Text("Search bookmarks") },
        onValueChange = {
            onAction(HomeAction.SearchQueryChanged(it))
        }
    )
}

@Preview
@Composable
fun SearchInputPreview() {
    SearchInput(Modifier) {}
}

@Composable
fun BookmarkItems(
    modifier: Modifier,
    bookmarks: List<Bookmark>,
    onAction: (HomeAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = bookmarks) {
            BookmarkItem(
                bookmark = it,
                onClick = { onAction(HomeAction.BookmarkClick(it.id)) },
                onDeleteClick = { onAction(HomeAction.DeleteBookmarkClick(it.id)) }
            )
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
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        shape = RoundedCornerShape(40.dp),
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer() {
                clip = false
            }
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "empty icon",
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            Text(text = "No Bookmarks")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStatePreview() {
    EmptyState()
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
        modifier = Modifier.padding(WindowInsets.safeDrawing.asPaddingValues()),
        onRequestPermissions = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeContentEmptyPreview() {
    HomeContent(
        state = HomeUiState(bookmarks = emptyList()),
        onAction = {},
        modifier = Modifier.padding(WindowInsets.safeDrawing.asPaddingValues()),
        onRequestPermissions = {}
    )
}
