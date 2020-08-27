package com.viveksharma.bookreadtracker.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.viveksharma.bookreadtracker.db.BookDAO
import com.viveksharma.bookreadtracker.db.DayStats
import com.viveksharma.bookreadtracker.db.DayStatsDao
import com.viveksharma.bookreadtracker.other.BookTrackingUtility
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject

class TimelineRepository
@Inject
constructor(
    private val bookDAO: BookDAO,
    private val dayStatsDao: DayStatsDao
) {

    fun getReadingBooksSortedByPriority() = bookDAO.getReadingBooksSortedByPriority()

    fun getFinishedBooksSortedByDate() = bookDAO.getFinishedBooksSortedByDate()

    fun getDayStats() = dayStatsDao.getDayStats()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateOrInsertDayStats(dayStats: DayStats) {
        val dayStatsList = dayStatsDao.getDayStatsByIdOrDayName(dayStats.dayID, dayStats.dayName)

        val today =
            BookTrackingUtility.getZonedDateTime(System.currentTimeMillis(), ChronoUnit.DAYS)
        val weekStart = today.minusDays(6).truncatedTo(ChronoUnit.DAYS)

        if (dayStatsList.isEmpty()) {
            dayStatsDao.insert(dayStats)
        } else {
            dayStatsList.forEach {
                //if (it.dayID.dayOfYear != dayStats.dayID.dayOfYear && it.dayName == dayStats.dayName) {
                if (it.dayID.toLocalDate().isBefore(weekStart.toLocalDate())) {
                    dayStatsDao.delete(it)
                    dayStatsDao.insert(dayStats)
                } else if (it.dayID.dayOfWeek == dayStats.dayID.dayOfWeek) {
                    dayStatsDao.update(dayStats.dayID, dayStats.totalPagesRead)
                }
            }
        }
    }
}