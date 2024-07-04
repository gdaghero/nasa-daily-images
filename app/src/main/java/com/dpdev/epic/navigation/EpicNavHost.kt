package com.dpdev.epic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.dpdev.epic.ui.EpicAppState
import com.dpdev.epic.ui.features.dailyimages.navigation.dailyImagesNavigationRoute
import com.dpdev.epic.ui.features.dailyimages.navigation.dailyImagesScreen
import com.dpdev.epic.ui.features.daydetail.navigation.dayDetailScreen
import com.dpdev.epic.ui.features.daydetail.navigation.navigateToDayDetail
import com.dpdev.epic.ui.features.imagedetail.navigation.imageDetailScreen
import com.dpdev.epic.ui.features.imagedetail.navigation.navigateToImageDetail

@Composable
fun EpicNavHost(
    modifier: Modifier = Modifier,
    appState: EpicAppState,
    startDestination: String = dailyImagesNavigationRoute
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        dailyImagesScreen(
            onDayClick = navController::navigateToDayDetail
        )
        dayDetailScreen(
            onBackPress = navController::popBackStack,
            onImageClick = navController::navigateToImageDetail
        )
        imageDetailScreen(
            onBackClick = navController::popBackStack
        )
    }
}
