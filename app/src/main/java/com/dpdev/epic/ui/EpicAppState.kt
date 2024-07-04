package com.dpdev.epic.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberEpicAppState(
    navController: NavHostController = rememberNavController()
): EpicAppState = remember(
    navController
) {
    EpicAppState(navController)
}

@Stable
class EpicAppState(val navController: NavHostController)
