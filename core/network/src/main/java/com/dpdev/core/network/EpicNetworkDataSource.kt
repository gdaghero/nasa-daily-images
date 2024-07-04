package com.dpdev.core.network

import com.dpdev.core.network.model.NetworkDay
import com.dpdev.core.network.model.NetworkDownload
import com.dpdev.core.network.model.NetworkImage

interface EpicNetworkDataSource {

    suspend fun getDays(): List<NetworkDay>
    suspend fun getImages(date: String): List<NetworkImage>
    suspend fun downloadImage(
        year: String,
        month: String,
        day: String,
        imageName: String
    ): NetworkDownload
}
