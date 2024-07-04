package com.dpdev.core.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dpdev.core.model.Coordinates
import com.dpdev.core.model.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Entity(
    tableName = "images",
    foreignKeys = [
        ForeignKey(
            entity = DayEntity::class,
            parentColumns = ["date"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ImageEntity(
    @PrimaryKey
    val id: String,
    val dayId: String,
    val caption: String,
    val name: String,
    val version: String,
    val date: String,
    val latitude: Double,
    val longitude: Double,
    val path: String? = null
)

fun ImageEntity.asExternalModel(progress: Flow<Int> = emptyFlow()) = Image(
    id = id,
    caption = caption,
    name = name,
    version = version,
    date = date,
    coordinates = Coordinates(
        latitude = latitude,
        longitude = longitude
    ),
    status = if (path != null) {
        Image.Status.Downloaded(path = path)
    } else {
        Image.Status.Downloading(progress = progress)
    }
)
