package com.viveksharma.bookreadtracker.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.viveksharma.bookreadtracker.db.Book
import com.viveksharma.bookreadtracker.repositories.FinishedRepository
import com.viveksharma.bookreadtracker.repositories.ReadingRepository
import kotlinx.coroutines.launch

class FinishedViewModel
@ViewModelInject
constructor(
    private val finishedRepository: FinishedRepository
) : ViewModel() {

    val finishedBooksSortedByDate = finishedRepository.getFinishedBooksSortedByDate()

    fun insertBook(book: Book) = viewModelScope.launch {
        finishedRepository.insertBook(book)
    }

    fun deleteBook(book: Book) = viewModelScope.launch {
        finishedRepository.deleteBook(book)
    }
}