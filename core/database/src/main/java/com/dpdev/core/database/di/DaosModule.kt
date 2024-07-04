package com.dpdev.core.database.di

import com.dpdev.core.database.EpicDatabase
import com.dpdev.core.database.dao.DayDao
import com.dpdev.core.database.dao.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesDayDao(database: EpicDatabase): DayDao = database.dayDao()

    @Provides
    fun providesImageDao(database: EpicDatabase): ImageDao = database.imageDao()
}
