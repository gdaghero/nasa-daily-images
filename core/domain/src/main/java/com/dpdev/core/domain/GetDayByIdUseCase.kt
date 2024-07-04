package com.dpdev.core.domain

import com.dpdev.core.model.Day
import com.dpdev.epic.core.data.repository.DaysRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDayByIdUseCase @Inject constructor(
    private val daysRepository: DaysRepository
) {

    operator fun invoke(id: String): Flow<Day> =
        daysRepository.getDay(id = id)
}
