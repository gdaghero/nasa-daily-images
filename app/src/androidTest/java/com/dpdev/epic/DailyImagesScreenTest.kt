package com.dpdev.epic

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.dpdev.core.model.Day
import com.dpdev.epic.ui.features.dailyimages.DailyImagesScreen
import com.dpdev.epic.ui.features.dailyimages.DailyImagesUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DailyImagesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var dayStatusFormat: String
    private lateinit var dayStatusDownloading: String

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            dayStatusFormat = getString(R.string.item_daily_images_status_format)
            dayStatusDownloading = getString(R.string.item_daily_images_status_pending)
        }
    }

    @Test
    fun progressIndicator_whenScreenIsLoading_isShown() {
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Loading,
                onClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("dailyImages:loading")
            .assertExists()
    }

    @Test
    fun progressIndicator_whenScreenIsSuccess_isNotShown() {
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(days = emptyList()),
                onClick = {}
            )
        }

        composeTestRule
            .onNodeWithTag("dailyImages:loading")
            .assertDoesNotExist()
    }

    @Test
    fun day_whenScreenIsSuccess_isShown() {
        // given
        val day = Day(
            date = "12-12-2023",
            name = "Friday",
            status = Day.Status.Pending
        )

        // when
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(days = listOf(day)),
                onClick = {}
            )
        }

        // then
        composeTestRule
            .onNodeWithText(day.date)
            .assertExists()

        composeTestRule
            .onNodeWithText(day.name)
            .assertExists()
    }

    @Test
    fun day_whenStatusIsCompleted_isShown() {
        // given
        val count = 10
        val day = Day(
            date = "12-12-2023",
            name = "Friday",
            status = Day.Status.Completed(count)
        )

        // when
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(days = listOf(day)),
                onClick = {}
            )
        }

        // then
        val expected = String.format(format = dayStatusFormat, count, count)
        composeTestRule
            .onNodeWithText(expected)
            .assertExists()
    }

    @Test
    fun day_whenStatusIsInProgress_IsShown() {
        // given
        val total = 10
        val completed = 5
        val day = Day(
            date = "12-12-2023",
            name = "Friday",
            status = Day.Status.InProgress(completedCount = completed, totalCount = total)
        )

        // when
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(days = listOf(day)),
                onClick = {}
            )
        }

        // then
        val expected = String.format(format = dayStatusFormat, completed, total)
        composeTestRule
            .onNodeWithText(expected)
            .assertExists()
    }

    @Test
    fun day_whenStatusIsPending_isShown() {
        // given
        val day = Day(
            date = "12-12-2023",
            name = "Friday",
            status = Day.Status.Pending
        )

        // when
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(days = listOf(day)),
                onClick = {}
            )
        }

        // then
        composeTestRule
            .onNodeWithText(dayStatusDownloading)
            .assertExists()
    }

    @Test
    fun dayIcon_whenStatusIsCompleted_isShown() {
        // given
        val day = Day(
            date = "12-12-2023",
            name = "Friday",
            status = Day.Status.Completed(10)
        )

        // when
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(days = listOf(day)),
                onClick = {}
            )
        }

        // then
        composeTestRule
            .onNodeWithTag("dailyImages:statusIcon", useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun dayIcon_whenStatusIsInProgress_isShown() {
        // given
        val day = Day(
            date = "12-12-2023",
            name = "Friday",
            status = Day.Status.InProgress(8, 10)
        )

        // when
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(days = listOf(day)),
                onClick = {}
            )
        }

        // then
        composeTestRule
            .onNodeWithTag("dailyImages:statusIcon", useUnmergedTree = true)
            .assertExists()
    }

    @Test
    fun dayIcon_whenStatusIsPending_isNotShown() {
        // given
        val day = Day(
            date = "12-12-2023",
            name = "Friday",
            status = Day.Status.Pending
        )

        // when
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(days = listOf(day)),
                onClick = {}
            )
        }

        // then
        composeTestRule
            .onNodeWithTag("dailyImages:statusIcon")
            .assertDoesNotExist()
    }

    @Test
    fun day_whenClickIsPerformed_isCalled() {
        // given
        val day = Day(
            date = "12-12-2023",
            name = "Friday",
            status = Day.Status.Pending
        )
        var clicked = false

        // when
        composeTestRule.setContent {
            DailyImagesScreen(
                dailyImagesUiState = DailyImagesUiState.Success(listOf(day)),
                onClick = { clicked = true }
            )
        }

        // then
        composeTestRule
            .onNodeWithTag("dailyImages:day${day.date}")
            .performClick()

        assert(clicked)
    }
}
