package com.dpdev.epic.core.data.repository

import com.dpdev.core.database.dao.DayDao
import com.dpdev.core.database.model.DaysWithImages
import com.dpdev.core.database.model.asExternalModel
import com.dpdev.core.model.Day
import com.dpdev.core.network.EpicNetworkDataSource
import com.dpdev.core.network.model.NetworkDay
import com.dpdev.epic.core.data.model.asEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineFirstDaysRepository @Inject constructor(
    private val network: EpicNetworkDataSource,
    private val dayDao: DayDao,
    private val ioDispatcher: CoroutineDispatcher
) : DaysRepository {

    override fun getDays(): Flow<List<Day>> =
        dayDao.getDaysEntitiesWithImages()
            .map { it.map(DaysWithImages::asExternalModel) }

    override fun getDay(id: String): Flow<Day> =
        dayDao.getDayWithImagesEntity(id = id)
            .map(DaysWithImages::asExternalModel)

    override suspend fun syncDays() {
        withContext(ioDispatcher) {
            val currentDays = dayDao.getOneOffDays()
            val days = network.getDays()
            if (currentDays.size != days.size) {
                dayDao.insertOrIgnoreDays(dayEntities = days.map(NetworkDay::asEntity))
            }
        }
    }
}
