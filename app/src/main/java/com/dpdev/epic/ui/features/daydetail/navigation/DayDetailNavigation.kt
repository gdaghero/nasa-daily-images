package com.dpdev.epic.ui.features.daydetail.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dpdev.epic.ui.features.daydetail.DayDetailRoute
import java.net.URLDecoder
import java.net.URLEncoder

const val dayDetailNavigationRoute = "day_detail_route"
const val dayDateArg = "dayDate"

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

class DayDetailArgs(val date: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[dayDateArg]),
                    URL_CHARACTER_ENCODING
                )
            )
}

fun NavController.navigateToDayDetail(date: String) {
    val encodedDate = URLEncoder.encode(date, URL_CHARACTER_ENCODING)
    navigate(route = "$dayDetailNavigationRoute/$encodedDate") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.dayDetailScreen(
    onBackPress: () -> Unit,
    onImageClick: (String) -> Unit
) {
    composable(
        route = "$dayDetailNavigationRoute/{$dayDateArg}",
        arguments = listOf(
            navArgument(dayDateArg) { type = NavType.StringType },
        )
    ) {
        DayDetailRoute(
            onBackPress = onBackPress,
            onImageClick = onImageClick
        )
    }
}
