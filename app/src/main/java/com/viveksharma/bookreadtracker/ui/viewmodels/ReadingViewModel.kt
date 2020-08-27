package com.viveksharma.bookreadtracker.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viveksharma.bookreadtracker.db.Book
import com.viveksharma.bookreadtracker.repositories.ReadingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReadingViewModel
@ViewModelInject
constructor(
    private val readingRepository: ReadingRepository
) : ViewModel() {

    val readingBooksSortedByPriority = readingRepository.getReadingBooksSortedByPriority()

    fun insertBook(book: Book) = viewModelScope.launch {
        readingRepository.insertBook(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch {
        readingRepository.deleteBook(book)
    }

    fun updatePagesReadAndTimeRead(pagesRead: Int, timeReadInSec: Long, id: Int) =
        viewModelScope.launch {
            readingRepository.updatePagesReadAndTimeRead(pagesRead, timeReadInSec, id)
        }

    fun updateFinishedReviewAndTimestamp(review: String, dateTimestamp: Long, id: Int) =
        viewModelScope.launch {
            readingRepository.updateFinishedReviewAndTimestamp(review, dateTimestamp, id)
        }

    val timeReadInMillis = MutableLiveData<Long>()
    val isTracking = MutableLiveData<Boolean>()
    private var timeRead = 0L
    private var timeStarted = 0L
    private var lapTime = 0L

    fun postInitialValuesOfTimer() {
        timeReadInMillis.postValue(0L)
        isTracking.postValue(false)
        timeRead = 0L
        timeStarted = 0L
        lapTime = 0L
    }

    fun startTimer() {
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()

        CoroutineScope(Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeReadInMillis.postValue(timeRead + lapTime)
                delay(50L)
            }
            timeRead += lapTime
        }
    }

    fun pauseTimer() {
        isTracking.postValue(false)
    }
}