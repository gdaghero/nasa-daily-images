package com.dpdev.epic.ui.features.imagedetail.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dpdev.epic.ui.features.imagedetail.ImageDetailRoute
import java.net.URLDecoder
import java.net.URLEncoder


const val imageDetailNavigationRoute = "image_detail_route"
const val imageIdArg = "imageId"

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

class ImageDetailArgs(val imageId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[imageIdArg]),
                    URL_CHARACTER_ENCODING
                )
            )
}


fun NavController.navigateToImageDetail(id: String) {
    val encodedId = URLEncoder.encode(id, URL_CHARACTER_ENCODING)
    navigate(route = "$imageDetailNavigationRoute/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.imageDetailScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "$imageDetailNavigationRoute/{$imageIdArg}",
        arguments = listOf(
            navArgument(imageIdArg) { type = NavType.StringType },
        )
    ) {
        ImageDetailRoute(
            onBackClick = onBackClick
        )
    }
}
