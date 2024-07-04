package com.dpdev.epic.ui.features.daydetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpdev.core.domain.GetDayByIdUseCase
import com.dpdev.core.domain.GetImagesByDayUseCase
import com.dpdev.core.domain.SyncImagesUseCase
import com.dpdev.core.model.Day
import com.dpdev.core.model.Image
import com.dpdev.epic.ui.features.daydetail.navigation.DayDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DayDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    syncImagesUseCase: SyncImagesUseCase,
    getImagesUseCase: GetImagesByDayUseCase,
    getDayUseCase: GetDayByIdUseCase
) : ViewModel() {

    private val dayDetailArgs: DayDetailArgs = DayDetailArgs(savedStateHandle)
    private val dayDate = dayDetailArgs.date
    private val _errorStream: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val imagesUiState: StateFlow<ImagesUiState> = combine(
        getImagesUseCase(dayId = dayDate),
        _errorStream,
        ::Pair
    )
        .map { (images, error) ->
            when {
                images.isEmpty() && error -> ImagesUiState.Error
                images.isEmpty() -> ImagesUiState.Loading
                else -> ImagesUiState.Success(images = images)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ImagesUiState.Loading
        )

    val dayUiState: StateFlow<DayUiState> = getDayUseCase(id = dayDate)
        .map { DayUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = DayUiState.Loading
        )

    init {
        viewModelScope.launch {
            runCatching { syncImagesUseCase(date = dayDate) }
                .onFailure {
                    Log.e(TAG, "syncImagesUseCase", it)
                    _errorStream.value = true
                }
        }
    }

    companion object {
        private const val TAG = "DayDetailViewModel"
    }
}

sealed interface ImagesUiState {
    data class Success(val images: List<Image>) : ImagesUiState
    data object Loading : ImagesUiState
    data object Error : ImagesUiState
}

sealed interface DayUiState {
    data object Loading : DayUiState
    data class Success(val day: Day) : DayUiState
}
