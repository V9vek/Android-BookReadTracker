package com.viveksharma.bookreadtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.viveksharma.bookreadtracker.R
import com.viveksharma.bookreadtracker.db.Book
import com.viveksharma.bookreadtracker.other.BookTrackingUtility
import kotlinx.android.synthetic.main.item_finished_book.view.ivCircle
import kotlinx.android.synthetic.main.item_finished_book.view.tvFinishedDate
import kotlinx.android.synthetic.main.item_finished_book.view.tvFinishedReview
import kotlinx.android.synthetic.main.item_reading_book.view.tvAuthor
import kotlinx.android.synthetic.main.item_reading_book.view.tvTitle

class FinishedBooksAdapter : RecyclerView.Adapter<FinishedBooksAdapter.FinishedBookViewHolder>() {

    //DiffUtil
    private var differCallback = object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Book>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedBookViewHolder {
        return FinishedBookViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_finished_book, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FinishedBookViewHolder, position: Int) {
        val currentBook = differ.currentList[position]
        holder.bind(currentBook)
    }

    //ViewHolder
    inner class FinishedBookViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(book: Book) {
            itemView.apply {
                tvTitle.text = book.title
                tvAuthor.text = book.author

                if (book.finishedReview?.isNotEmpty()!!) {
                    tvFinishedReview.text = book.finishedReview
                }

                ivCircle.background.setTint(
                    BookTrackingUtility.getPriorityColor(context, book.priority)
                )

                tvFinishedDate.text =
                    "Finished on ${BookTrackingUtility.getFormattedFinishedDate(book.finishedTimestamp)}"
            }
        }
    }
}