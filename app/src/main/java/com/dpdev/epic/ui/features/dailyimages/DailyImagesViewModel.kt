package com.dpdev.epic.ui.features.dailyimages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpdev.core.domain.GetDaysUseCase
import com.dpdev.core.model.Day
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DailyImagesViewModel @Inject constructor(
    getDaysUseCase: GetDaysUseCase
) : ViewModel() {

    val uiState: StateFlow<DailyImagesUiState> = getDaysUseCase()
        .map { DailyImagesUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DailyImagesUiState.Loading
        )
}

sealed interface DailyImagesUiState {
    data class Success(val days: List<Day>) : DailyImagesUiState
    data object Error : DailyImagesUiState
    data object Loading : DailyImagesUiState
}
