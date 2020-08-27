package com.viveksharma.bookreadtracker.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viveksharma.bookreadtracker.db.DayStats
import com.viveksharma.bookreadtracker.repositories.TimelineRepository
import kotlinx.coroutines.launch

class TimelineViewModel
@ViewModelInject
constructor(
    private val timelineRepository: TimelineRepository
) : ViewModel() {

    val readingBooksSortedByPriority = timelineRepository.getReadingBooksSortedByPriority()

    val finishedBooksSortedByDate = timelineRepository.getFinishedBooksSortedByDate()

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateOrInsertDayStats(dayStats: DayStats) = viewModelScope.launch {
        timelineRepository.updateOrInsertDayStats(dayStats)
    }

    val allDayStats = timelineRepository.getDayStats()
}