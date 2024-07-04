package com.dpdev.epic.core.data.repository

import com.dpdev.core.model.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun syncImages(date: String)
    fun getImageById(id: String): Flow<Image>
    fun getImagesByDay(dayId: String): Flow<List<Image>>
}
