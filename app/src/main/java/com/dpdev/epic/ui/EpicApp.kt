package com.dpdev.epic.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dpdev.epic.navigation.EpicNavHost

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EpicApp(appState: EpicAppState = rememberEpicAppState()) {
    Surface {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { contentPadding ->
            EpicNavHost(
                Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(contentPadding)
                    .statusBarsPadding(),
                appState = appState
            )
        }
    }
}
