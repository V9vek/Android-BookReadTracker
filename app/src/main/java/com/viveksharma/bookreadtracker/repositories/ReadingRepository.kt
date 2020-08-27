package com.viveksharma.bookreadtracker.repositories

import com.viveksharma.bookreadtracker.db.Book
import com.viveksharma.bookreadtracker.db.BookDAO
import javax.inject.Inject

class ReadingRepository
@Inject
constructor(
    private val bookDAO: BookDAO
) {

    suspend fun insertBook(book: Book) = bookDAO.insertBook(book)

    suspend fun deleteBook(book: Book) = bookDAO.deleteBook(book)

    fun getReadingBooksSortedByPriority() = bookDAO.getReadingBooksSortedByPriority()

    suspend fun updatePagesReadAndTimeRead(pagesRead: Int, timeReadInSec: Long, id: Int) =
        bookDAO.updatePagesReadAndTimeRead(pagesRead, timeReadInSec, id)

    suspend fun updateFinishedReviewAndTimestamp(review: String, dateTimestamp: Long, id: Int) =
        bookDAO.updateFinishedReviewAndTimestamp(review, dateTimestamp, id)
}