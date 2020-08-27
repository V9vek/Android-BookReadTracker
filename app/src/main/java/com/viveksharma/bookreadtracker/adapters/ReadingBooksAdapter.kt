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
import kotlinx.android.synthetic.main.item_reading_book.view.*

class ReadingBooksAdapter : RecyclerView.Adapter<ReadingBooksAdapter.ReadingBookViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingBookViewHolder {
        return ReadingBookViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reading_book, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ReadingBookViewHolder, position: Int) {
        val currentBook = differ.currentList[position]
        holder.bind(currentBook)
    }

    fun setOnItemClickListener(listener: (Book) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemClickListener: ((Book) -> Unit)? = null

    //ViewHolder
    inner class ReadingBookViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(book: Book) {
            itemView.apply {
                tvTitle.text = book.title
                tvAuthor.text = book.author
                ivPriority.background.setTint(
                    BookTrackingUtility.getPriorityColor(
                        context, book.priority
                    )
                )
                trackingProgressBar.apply {
                    max = book.totalPages
                    progress = book.pagesRead
                }

                setOnClickListener {
                    onItemClickListener?.let {
                        it(book)
                    }
                }
            }
        }
    }
}