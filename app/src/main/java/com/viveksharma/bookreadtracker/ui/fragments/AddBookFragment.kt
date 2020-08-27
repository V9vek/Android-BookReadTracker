package com.viveksharma.bookreadtracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.viveksharma.bookreadtracker.R
import com.viveksharma.bookreadtracker.db.Book
import com.viveksharma.bookreadtracker.ui.viewmodels.ReadingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_book.*

@AndroidEntryPoint
class AddBookFragment : Fragment(R.layout.fragment_add_book) {

    private val viewModel: ReadingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSave.setOnClickListener {
            getBookDetails()
        }
    }

    private fun getBookDetails() {
        if (!validateTitle() || !validateAuthor() || !validateTotalPages() || getReadingPriority() == -1) {
            return
        }

        val title = etTitle.editText?.text.toString().trim()
        val author = etAuthor.editText?.text.toString()
        val totalPages = etTotalPages.editText?.text.toString()
        val priority = getReadingPriority()

        val book = Book(title, author, totalPages.toInt(), priority)
        saveBookToDb(book)
    }

    private fun validateTitle(): Boolean {
        val titleInput = etTitle.editText?.text.toString().trim()
        return when {
            titleInput.isEmpty() -> {
                etTitle.error = "Title can't be empty"
                false
            }
            titleInput.length > 50 -> {
                etTitle.error = "Title too long"
                return false
            }
            else -> {
                etTitle.error = null
                true
            }
        }
    }

    private fun validateAuthor(): Boolean {
        val authorInput = etAuthor.editText?.text.toString().trim()
        return when {
            authorInput.isEmpty() -> {
                etAuthor.error = "Author can't be empty"
                false
            }
            authorInput.length > 50 -> {
                etAuthor.error = "Author name too long"
                return false
            }
            else -> {
                etTitle.error = null
                true
            }
        }
    }

    private fun validateTotalPages(): Boolean {
        val totalPagesInput = etTotalPages.editText?.text.toString().trim()
        return when {
            totalPagesInput.isBlank() -> {
                etTotalPages.error = "Total Pages can't be empty"
                false
            }
            totalPagesInput.toInt() <= 0 -> {
                etTotalPages.error = "Total Pages are too less"
                false
            }
            else -> {
                etTotalPages.error = null
                true
            }
        }
    }

    private fun getReadingPriority(): Int {
        return when (radioGroup.checkedRadioButtonId) {
            R.id.rbHigh -> 0
            R.id.rbMedium -> 1
            R.id.rbLow -> 2
            else -> -1
        }
    }

    private fun saveBookToDb(book: Book) {
        viewModel.insertBook(book)

        Snackbar.make(
            requireActivity().findViewById(R.id.rootView),
            "Book Added Successfully",
            Snackbar.LENGTH_SHORT
        ).show()

        findNavController().navigate(R.id.action_addBookFragment_to_readingFragment)
    }
}