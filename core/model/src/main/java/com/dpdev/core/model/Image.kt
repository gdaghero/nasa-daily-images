package com.dpdev.core.model

import kotlinx.coroutines.flow.Flow

data class Image(
    val id: String,
    val caption: String,
    val name: String,
    val version: String,
    val date: String,
    val coordinates: Coordinates,
    val status: Status
) {
    sealed interface Status {
        data class Downloading(val progress: Flow<Int>) : Status
        data class Downloaded(val path: String) : Status
    }
}
