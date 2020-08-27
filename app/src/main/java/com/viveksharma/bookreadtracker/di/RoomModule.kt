package com.viveksharma.bookreadtracker.di

import android.content.Context
import androidx.room.Room
import com.viveksharma.bookreadtracker.db.BookTrackingDatabase
import com.viveksharma.bookreadtracker.other.Constants.BOOK_TRACKING_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideBookTrackingDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        BookTrackingDatabase::class.java,
        BOOK_TRACKING_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideBookDao(db: BookTrackingDatabase) = db.getBookDao()

    @Singleton
    @Provides
    fun provideDayStatsDao(db: BookTrackingDatabase) = db.getDayStatsDao()
}