package com.dpdev.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dpdev.core.database.dao.DayDao
import com.dpdev.core.database.dao.ImageDao
import com.dpdev.core.database.model.DayEntity
import com.dpdev.core.database.model.ImageEntity

@Database(
    entities = [
        DayEntity::class,
        ImageEntity::class
    ],
    version = 1
)
abstract class EpicDatabase : RoomDatabase() {
    abstract fun dayDao(): DayDao
    abstract fun imageDao(): ImageDao
}
