package com.dpdev.epic.core.data.di

import com.dpdev.epic.core.data.repository.DaysRepository
import com.dpdev.epic.core.data.repository.ImageRepository
import com.dpdev.epic.core.data.repository.OfflineFirstDaysRepository
import com.dpdev.epic.core.data.repository.OfflineFirstImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindsDaysRepository(
        daysRepository: OfflineFirstDaysRepository
    ): DaysRepository

    @Binds
    @Singleton
    fun bindsImageRepository(
        imageRepository: OfflineFirstImageRepository
    ): ImageRepository
}
