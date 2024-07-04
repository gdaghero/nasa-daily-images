package com.dpdev.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dpdev.core.database.model.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query(value = "SELECT * FROM images WHERE dayId = :dayId")
    fun getImagesEntitiesByDayId(dayId: String): Flow<List<ImageEntity>>

    @Query(value = "SELECT * FROM images WHERE dayId = :dayId AND path is NULL")
    suspend fun getImagesEntitiesPendingDownload(dayId: String): List<ImageEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreImages(imageEntities: List<ImageEntity>)

    @Query(value = "UPDATE images SET path = :path WHERE id = :id ")
    suspend fun updateImagePath(id: String, path: String)

    @Query(value = "SELECT * FROM images WHERE id = :id")
    fun getImageById(id: String): Flow<ImageEntity>
}
