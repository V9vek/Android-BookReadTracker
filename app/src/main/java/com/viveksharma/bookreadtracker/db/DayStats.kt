package com.viveksharma.bookreadtracker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.ZonedDateTime

@Entity(tableName = "daystats_table")
data class DayStats(
    @PrimaryKey
    val dayID: ZonedDateTime,
    val dayName: Int,
    var totalPagesRead: Int = 0
)