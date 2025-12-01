package engineer.filip.hoarder.ui.deleteconfirmation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import engineer.filip.hoarder.data.model.Bookmark

/**
 * Screen-level composable for delete confirmation bottom sheet.
 * Handles ViewModel and navigation events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationScreen(
    viewModel: DeleteConfirmationViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Observe one-time events for navigation
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is DeleteConfirmationEvent.Dismissed -> onDismiss()
                is DeleteConfirmationEvent.DeleteConfirmed -> onDeleteConfirmed()
            }
        }
    }

    DeleteConfirmationBottomSheet(
        state = uiState,
        sheetState = sheetState,
        onAction = viewModel::onAction,
        onDismissRequest = { viewModel.onAction(DeleteConfirmationAction.Cancel) }
    )
}

/**
 * Pure UI composable for delete confirmation bottom sheet.
 * Fully testable without ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteConfirmationBottomSheet(
    state: DeleteConfirmationUiState,
    sheetState: SheetState,
    onAction: (DeleteConfirmationAction) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        contentWindowInsets = { WindowInsets(0) }
    ) {
        DeleteConfirmationContent(
            state = state,
            onAction = onAction,
            modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues())
        )
    }
}

/**
 * Content composable for the delete confirmation dialog.
 * Can be used independently for previews and testing.
 */
@Composable
fun DeleteConfirmationContent(
    state: DeleteConfirmationUiState,
    onAction: (DeleteConfirmationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Delete Bookmark?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            state.bookmark?.let { bookmark ->
                Text(
                    text = "Are you sure you want to delete \"${bookmark.title}\"?",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = bookmark.url,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { onAction(DeleteConfirmationAction.Cancel) },
                modifier = Modifier.weight(1f),
                enabled = !state.isDeleting
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onAction(DeleteConfirmationAction.ConfirmDelete) },
                modifier = Modifier.weight(1f),
                enabled = !state.isDeleting && state.bookmark != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                if (state.isDeleting) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(16.dp).width(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    Text("Delete")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteConfirmationContentPreview() {
    DeleteConfirmationContent(
        state = DeleteConfirmationUiState(
            bookmark = Bookmark(
                id = "1",
                title = "Kotlin Documentation",
                url = "https://kotlinlang.org/docs/home.html",
                notes = "Great resource for learning Kotlin"
            )
        ),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun DeleteConfirmationContentLoadingPreview() {
    DeleteConfirmationContent(
        state = DeleteConfirmationUiState(isLoading = true),
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun DeleteConfirmationContentDeletingPreview() {
    DeleteConfirmationContent(
        state = DeleteConfirmationUiState(
            bookmark = Bookmark(
                id = "1",
                title = "Kotlin Documentation",
                url = "https://kotlinlang.org/docs/home.html",
                notes = ""
            ),
            isDeleting = true
        ),
        onAction = {}
    )
}

