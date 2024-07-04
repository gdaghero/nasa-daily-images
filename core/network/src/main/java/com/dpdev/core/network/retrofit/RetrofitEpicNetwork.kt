package com.dpdev.core.network.retrofit

import com.dpdev.core.network.BuildConfig
import com.dpdev.core.network.EpicNetworkDataSource
import com.dpdev.core.network.model.NetworkDay
import com.dpdev.core.network.model.NetworkDownload
import com.dpdev.core.network.model.NetworkImage
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import javax.inject.Inject
import javax.inject.Singleton

private interface RetrofitEpicNetworkApi {
    @GET(value = "/api/enhanced/all")
    suspend fun getDays(): List<NetworkDay>

    @GET(value = "api/enhanced/date/{date}")
    suspend fun getImages(@Path("date") date: String): List<NetworkImage>

    @Streaming
    @GET("archive/enhanced/{year}/{month}/{day}/png/{imageName}.png")
    suspend fun downloadImage(
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String,
        @Path("imageName") imageName: String
    ): ResponseBody
}

private const val EPIC_BASE_URL = BuildConfig.EPIC_BASE_URL

@Singleton
class RetrofitEpicNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : EpicNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(EPIC_BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitEpicNetworkApi::class.java)

    override suspend fun getDays(): List<NetworkDay> =
        networkApi.getDays()

    override suspend fun getImages(date: String): List<NetworkImage> =
        networkApi.getImages(date = date)

    override suspend fun downloadImage(
        year: String,
        month: String,
        day: String,
        imageName: String
    ): NetworkDownload {
        val responseBody = networkApi.downloadImage(
            year = year,
            month = month,
            day = day,
            imageName = imageName
        )
        return NetworkDownload(
            stream = responseBody.byteStream(),
            contentLength = responseBody.contentLength()
        )
    }
}
