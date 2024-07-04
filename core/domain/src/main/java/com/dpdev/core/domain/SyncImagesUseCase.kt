package com.dpdev.core.domain

import com.dpdev.epic.core.data.repository.ImageRepository
import javax.inject.Inject

class SyncImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(date: String) {
        imageRepository.syncImages(date = date)
    }
}
