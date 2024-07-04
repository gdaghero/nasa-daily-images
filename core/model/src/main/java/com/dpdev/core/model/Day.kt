package com.dpdev.core.model

data class Day(
    val date: String,
    val name: String,
    val status: Status
) {
    sealed class Status {
        data object Pending : Status()

        data class InProgress(
            val completedCount: Int,
            val totalCount: Int
        ) : Status()

        data class Completed(val totalCount: Int) : Status()
    }
}
