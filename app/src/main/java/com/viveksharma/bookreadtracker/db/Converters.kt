package com.viveksharma.bookreadtracker.db

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.viveksharma.bookreadtracker.other.BookTrackingUtility
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class Converters {

    @TypeConverter
    fun fromDate(date: ZonedDateTime): Long {
        return date.truncatedTo(ChronoUnit.DAYS).toInstant().toEpochMilli()
    }

    @TypeConverter
    fun toDate(millis: Long): ZonedDateTime {
        return BookTrackingUtility.getZonedDateTime(millis, ChronoUnit.DAYS)
    }
}