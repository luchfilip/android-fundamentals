package engineer.filip.hoarder

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun topAppBar_showsTitle() {
        composeRule.onNodeWithText("Hoarder").assertIsDisplayed()
    }

    @Test
    fun clickFab_addsItem() {
        composeRule.onNodeWithText("#0").performClick()
        composeRule.onNodeWithText("Bookmark Title").assertIsDisplayed()
    }

}