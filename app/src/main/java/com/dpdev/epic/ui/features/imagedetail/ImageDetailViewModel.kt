package com.dpdev.epic.ui.features.imagedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpdev.core.domain.GetImageByIdUseCase
import com.dpdev.core.model.Image
import com.dpdev.epic.ui.features.imagedetail.navigation.ImageDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getImageByIdUseCase: GetImageByIdUseCase
) : ViewModel() {

    private val imageDetailArgs: ImageDetailArgs = ImageDetailArgs(savedStateHandle)
    private val imageId = imageDetailArgs.imageId

    val imageUiState: StateFlow<ImageDetailUiState> =
        getImageByIdUseCase(id = imageId)
            .map { ImageDetailUiState.Success(image = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = ImageDetailUiState.Loading
            )

}

sealed interface ImageDetailUiState {
    data object Loading : ImageDetailUiState
    data class Success(val image: Image) : ImageDetailUiState
}
