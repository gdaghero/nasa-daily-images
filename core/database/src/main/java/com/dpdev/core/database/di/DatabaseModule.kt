package com.dpdev.core.database.di

import android.content.Context
import androidx.room.Room
import com.dpdev.core.database.EpicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesEpicDatabase(
        @ApplicationContext context: Context,
    ): EpicDatabase = Room.databaseBuilder(
        context,
        EpicDatabase::class.java,
        "epic-database",
    ).build()
}
