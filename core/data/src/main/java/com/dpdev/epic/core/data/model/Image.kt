package com.dpdev.epic.core.data.model

import com.dpdev.core.database.model.ImageEntity
import com.dpdev.core.network.model.NetworkImage

fun NetworkImage.asEntity(dayId: String) = ImageEntity(
    id = identifier,
    dayId = dayId,
    caption = caption,
    name = image,
    version = version,
    date = date,
    latitude = coordinates.lat,
    longitude = coordinates.lon
)
