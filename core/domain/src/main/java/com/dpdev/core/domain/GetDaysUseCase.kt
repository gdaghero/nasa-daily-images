package com.dpdev.core.domain

import com.dpdev.core.model.Day
import com.dpdev.epic.core.data.repository.DaysRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDaysUseCase @Inject constructor(
    private val daysRepository: DaysRepository
) {

    operator fun invoke(): Flow<List<Day>> =
        daysRepository.getDays()
}
