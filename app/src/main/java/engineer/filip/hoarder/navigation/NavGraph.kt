package engineer.filip.hoarder.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import engineer.filip.hoarder.ui.deleteconfirmation.DeleteConfirmationScreen
import engineer.filip.hoarder.ui.detail.DetailScreen
import engineer.filip.hoarder.ui.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Detail(val bookmarkId: String)

@Serializable
data class DeleteConfirmation(val bookmarkId: String)

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Home> {
            HomeScreen(
                onNavigateToDetail = { bookmarkId ->
                    navController.navigate(Detail(bookmarkId))
                },
                onNavigateToDeleteConfirmation = { bookmarkId ->
                    navController.navigate(DeleteConfirmation(bookmarkId))
                }
            )
        }

        composable<Detail> { backStackEntry ->
            DetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDeleteConfirmation = { bookmarkId ->
                    navController.navigate(DeleteConfirmation(bookmarkId))
                }
            )
        }

        dialog<DeleteConfirmation> { backStackEntry ->
            DeleteConfirmationScreen(
                onDismiss = { navController.popBackStack() },
                onDeleteConfirmed = {
                    // Pop back to home after deletion
                    navController.popBackStack(Home, inclusive = false)
                }
            )
        }
    }
}
