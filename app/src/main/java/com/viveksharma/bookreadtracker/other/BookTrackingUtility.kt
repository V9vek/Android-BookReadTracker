package com.viveksharma.bookreadtracker.other

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.viveksharma.bookreadtracker.R
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.temporal.ChronoUnit
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object BookTrackingUtility {

    fun getPriorityColor(context: Context, priority: Int): Int {
        return when (priority) {
            0 -> ContextCompat.getColor(context, R.color.colorHighPriority)
            1 -> ContextCompat.getColor(context, R.color.colorMediumPriority)
            2 -> ContextCompat.getColor(context, R.color.colorLowPriority)
            else -> ContextCompat.getColor(context, R.color.colorElsePriority)
        }
    }

    fun getFormattedStopwatchTime(ms: Long): String {
        var milliseconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getZonedDateTime(millis: Long, truncatedTo: ChronoUnit): ZonedDateTime =
        Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).truncatedTo(truncatedTo)

    fun getFormattedFinishedDate(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

        return dateFormat.format(calendar.time)
    }

    fun getTotalPagesReadString(totalPagesRead: Float): String {
        return "${totalPagesRead.toInt()} Pages"
    }
}