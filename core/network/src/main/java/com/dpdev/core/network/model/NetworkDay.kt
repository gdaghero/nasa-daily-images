package com.dpdev.core.network.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class NetworkDay(
    val date: LocalDate
)
