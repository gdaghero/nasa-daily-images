package com.dpdev.core.domain

import com.dpdev.epic.core.data.repository.DaysRepository
import javax.inject.Inject

class SyncDaysUseCase @Inject constructor(
    private val daysRepository: DaysRepository
) {

    suspend operator fun invoke() {
        daysRepository.syncDays()
    }
}
