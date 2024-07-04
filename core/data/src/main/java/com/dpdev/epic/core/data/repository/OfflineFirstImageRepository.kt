package com.dpdev.epic.core.data.repository

import android.content.Context
import com.dpdev.core.database.dao.ImageDao
import com.dpdev.core.database.model.ImageEntity
import com.dpdev.core.database.model.asExternalModel
import com.dpdev.core.model.Image
import com.dpdev.core.network.EpicNetworkDataSource
import com.dpdev.epic.core.data.model.asEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class OfflineFirstImageRepository @Inject constructor(
    private val network: EpicNetworkDataSource,
    private val imageDao: ImageDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val externalScope: CoroutineScope,
    @ApplicationContext private val context: Context
) : ImageRepository {

    private val imagesProgress: HashMap<String, MutableStateFlow<Int>> = hashMapOf()

    override fun getImageById(id: String): Flow<Image> =
        imageDao.getImageById(id = id)
            .map(ImageEntity::asExternalModel)

    override fun getImagesByDay(dayId: String): Flow<List<Image>> =
        imageDao.getImagesEntitiesByDayId(dayId = dayId)
            .map { images ->
                images.map { image ->
                    image.asExternalModel(imagesProgress.getOrPut(key = image.id) {
                        MutableStateFlow(value = 0)
                    })
                }
            }

    override suspend fun syncImages(date: String) {
        withContext(ioDispatcher) {
            val remoteImages = network.getImages(date = date)
            imageDao.insertOrIgnoreImages(
                imageEntities = remoteImages.map { it.asEntity(dayId = date) }
            )
            val pendingImages = imageDao.getImagesEntitiesPendingDownload(dayId = date)
            pendingImages.forEach { image ->
                externalScope.launch {
                    val path = downloadImage(id = image.id, name = image.name, date = image.date)
                    imageDao.updateImagePath(id = image.id, path = path)
                }
            }
        }
    }

    private suspend fun downloadImage(id: String, name: String, date: String): String {
        val sharedFlow = imagesProgress[id]
        val dateParts = date.split("-") /* TODO: use LocalDateTime instead of this hack */
        val dayPart = dateParts[2].split(" ")
        val destinationFile = File("${context.cacheDir}/$name.png")
        val download = network.downloadImage(
            year = dateParts[0],
            month = dateParts[1],
            day = dayPart[0],
            imageName = name
        )

        download.stream.use { inputStream ->
            destinationFile.outputStream().use { outputStream ->
                val totalBytes = download.contentLength
                val buffer = ByteArray(size = DEFAULT_BUFFER_SIZE)
                var progressBytes = 0L
                var bytes = inputStream.read(buffer)
                while (bytes >= 0) {
                    outputStream.write(buffer, 0, bytes)
                    progressBytes += bytes
                    bytes = inputStream.read(buffer)
                    sharedFlow?.value = ((progressBytes * 100) / totalBytes).toInt()
                }
            }
        }

        return destinationFile.path
    }
}
