package com.dpdev.core.domain

import com.dpdev.core.model.Image
import com.dpdev.epic.core.data.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImagesByDayUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    operator fun invoke(dayId: String): Flow<List<Image>> =
        imageRepository.getImagesByDay(dayId = dayId)
}
