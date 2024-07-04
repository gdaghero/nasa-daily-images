package com.dpdev.epic.ui.features.dailyimages.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dpdev.epic.ui.features.dailyimages.DailyImagesRoute

const val dailyImagesNavigationRoute = "daily_images_route"

fun NavGraphBuilder.dailyImagesScreen(onDayClick: (String) -> Unit) {
    composable(
        route = dailyImagesNavigationRoute,
    ) {
        DailyImagesRoute(onDayClick = onDayClick)
    }
}
