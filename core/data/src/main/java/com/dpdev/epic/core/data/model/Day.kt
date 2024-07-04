package com.dpdev.epic.core.data.model

import android.annotation.SuppressLint
import com.dpdev.core.database.model.DayEntity
import com.dpdev.core.network.model.NetworkDay
import java.time.format.TextStyle
import java.util.Locale

@SuppressLint("NewApi")
fun NetworkDay.asEntity() = DayEntity(
    name = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
    date = date.toString()
)
