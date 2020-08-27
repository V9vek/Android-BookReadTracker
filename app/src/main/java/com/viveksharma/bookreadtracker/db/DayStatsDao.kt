package com.viveksharma.bookreadtracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import org.threeten.bp.ZonedDateTime

@Dao
interface DayStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dayStats: DayStats)

    @Delete
    suspend fun delete(dayStats: DayStats)

    @Query("UPDATE daystats_table SET totalPagesRead = totalPagesRead + :pagesRead WHERE dayID = :dayId")
    suspend fun update(dayId: ZonedDateTime, pagesRead: Int)

    @Query("SELECT * FROM daystats_table where dayID = :dayId OR dayName = :dayName")
    suspend fun getDayStatsByIdOrDayName(dayId: ZonedDateTime, dayName: Int): List<DayStats>

    @Query("SELECT * FROM daystats_table")
    fun getDayStats(): LiveData<List<DayStats>>
}