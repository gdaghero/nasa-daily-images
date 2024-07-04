package com.dpdev.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCoordinate(
    val lat: Double,
    val lon: Double
)
