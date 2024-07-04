package com.dpdev.epic.core.data.repository

import com.dpdev.core.model.Day
import kotlinx.coroutines.flow.Flow

interface DaysRepository {

    suspend fun syncDays()
    fun getDays(): Flow<List<Day>>
    fun getDay(id: String): Flow<Day>
}
