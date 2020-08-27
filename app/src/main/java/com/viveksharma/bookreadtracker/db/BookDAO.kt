package com.viveksharma.bookreadtracker.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books_table WHERE pagesRead < totalPages ORDER BY priority")
    fun getReadingBooksSortedByPriority(): LiveData<List<Book>>

    @Query("SELECT * FROM books_table WHERE pagesRead = totalPages ORDER BY finishedTimestamp DESC")
    fun getFinishedBooksSortedByDate(): LiveData<List<Book>>

    @Query("SELECT * FROM books_table")
    fun getAllBooks(): LiveData<List<Book>>

    @Query("UPDATE books_table SET pagesRead = :pagesRead, timeReadInSec = timeReadInSec + :timeReadInSec WHERE id = :id")
    suspend fun updatePagesReadAndTimeRead(pagesRead: Int, timeReadInSec: Long, id: Int)

    @Query("UPDATE books_table SET finishedReview = :review, finishedTimestamp = :dateTimestamp WHERE id = :id")
    suspend fun updateFinishedReviewAndTimestamp(review: String, dateTimestamp: Long, id: Int)
}