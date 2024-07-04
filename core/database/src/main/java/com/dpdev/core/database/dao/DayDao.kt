package com.dpdev.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dpdev.core.database.model.DayEntity
import com.dpdev.core.database.model.DaysWithImages
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {

    @Transaction
    @Query("SELECT * FROM days ORDER BY date DESC")
    fun getDaysEntitiesWithImages(): Flow<List<DaysWithImages>>

    @Query(value = "SELECT * FROM days")
    suspend fun getOneOffDays(): List<DayEntity>

    @Query(value = "SELECT * FROM days WHERE :id = date")
    fun getDayWithImagesEntity(id: String): Flow<DaysWithImages>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreDays(dayEntities: List<DayEntity>)
}
