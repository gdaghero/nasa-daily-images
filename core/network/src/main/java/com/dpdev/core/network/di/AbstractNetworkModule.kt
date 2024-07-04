package com.dpdev.core.network.di

import com.dpdev.core.network.EpicNetworkDataSource
import com.dpdev.core.network.retrofit.RetrofitEpicNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface AbstractNetworkModule {

    @Binds
    fun bindsEpicNetworkDataSource(retrofitEpicNetwork: RetrofitEpicNetwork): EpicNetworkDataSource
}
