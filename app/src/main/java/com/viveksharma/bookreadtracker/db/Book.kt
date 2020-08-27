package com.viveksharma.bookreadtracker.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "books_table")
@Parcelize
data class Book(
    var title: String? = null,
    var author: String? = null,
    var totalPages: Int = 0,
    var priority: Int = 0,
    var pagesRead: Int = 0,
    var timeReadInSec: Long = 0L,
    var finishedReview: String? = null,
    var finishedTimestamp: Long = 0L
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}