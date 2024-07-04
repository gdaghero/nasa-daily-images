package com.dpdev.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.dpdev.core.model.Day

@Entity(tableName = "days")
data class DayEntity(
    @PrimaryKey
    val date: String,
    val name: String
)

data class DaysWithImages(
    @Embedded val day: DayEntity,
    @Relation(
        parentColumn = "date",
        entityColumn = "dayId"
    )
    val images: List<ImageEntity>
)

fun DaysWithImages.asExternalModel() = Day(
    name = day.name,
    date = day.date,
    status = when {
        images.isEmpty() -> Day.Status.Pending
        images.any { it.path == null } -> Day.Status.InProgress(
            completedCount = images.count { it.path != null },
            totalCount = images.count()
        )

        else -> Day.Status.Completed(totalCount = images.count())
    }
)
