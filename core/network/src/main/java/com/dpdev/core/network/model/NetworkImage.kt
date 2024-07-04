package com.dpdev.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkImage(
    val identifier: String,
    val caption: String,
    val image: String,
    val version: String,
    val date: String,
    @SerialName("centroid_coordinates")
    val coordinates: NetworkCoordinate
)
