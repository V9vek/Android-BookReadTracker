package com.viveksharma.bookreadtracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Book::class, DayStats::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BookTrackingDatabase : RoomDatabase() {

    abstract fun getBookDao(): BookDAO

    abstract fun getDayStatsDao(): DayStatsDao
}