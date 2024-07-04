package com.dpdev.epic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpdev.core.domain.GetDaysUseCase
import com.dpdev.core.domain.SyncDaysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    syncDaysUseCase: SyncDaysUseCase,
    getDaysUseCase: GetDaysUseCase
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = getDaysUseCase()
        .map {
            if (it.isNotEmpty()) {
                MainActivityUiState.Success
            } else {
                MainActivityUiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainActivityUiState.Loading
        )

    init {
        viewModelScope.launch {
            runCatching { syncDaysUseCase() }
                .onFailure { Log.e(TAG, "syncDaysUseCase", it) }
        }
    }

    companion object {
        private const val TAG = "MainActivityViewModel"
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data object Success : MainActivityUiState
}
