package com.viveksharma.bookreadtracker.repositories

import com.viveksharma.bookreadtracker.db.Book
import com.viveksharma.bookreadtracker.db.BookDAO
import javax.inject.Inject

class FinishedRepository
@Inject
constructor(
    private val bookDAO: BookDAO
) {

    suspend fun insertBook(book: Book) = bookDAO.insertBook(book)

    suspend fun deleteBook(book: Book) = bookDAO.deleteBook(book)

    fun getFinishedBooksSortedByDate() = bookDAO.getFinishedBooksSortedByDate()
}