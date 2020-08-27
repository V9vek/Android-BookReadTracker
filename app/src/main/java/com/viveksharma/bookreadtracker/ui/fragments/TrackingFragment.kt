package com.viveksharma.bookreadtracker.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.textfield.TextInputLayout
import com.viveksharma.bookreadtracker.R
import com.viveksharma.bookreadtracker.db.Book
import com.viveksharma.bookreadtracker.db.DayStats
import com.viveksharma.bookreadtracker.other.BookTrackingUtility
import com.viveksharma.bookreadtracker.ui.viewmodels.ReadingViewModel
import com.viveksharma.bookreadtracker.ui.viewmodels.TimelineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import org.threeten.bp.ZonedDateTime
import java.util.*

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: ReadingViewModel by viewModels()
    private val timelineViewModel: TimelineViewModel by viewModels()

    private val args: TrackingFragmentArgs by navArgs()

    private var isTracking: Boolean = false

    private var currentTimeInSeconds: Long = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val book = args.book

        setBookInformation(book)

        btnToggle.setOnClickListener {
            toggleRead()
        }

        btnDone.setOnClickListener {
            showPagesReadDialogAndUpdate(book)
        }

        subscribeToObservers()
    }

    private fun setBookInformation(book: Book) {
        tvTitle.text = book.title
        tvAuthor.text = book.author
        tvPagesRead.text = "${book.pagesRead} Pages Read"
        tvTotalPages.text = "${book.totalPages} Total Pages"

        var readingSpeed = 0F
        if (book.timeReadInSec > 0) {
            readingSpeed = book.pagesRead / (book.timeReadInSec / 60F)
        }
        tvReadingSpeed.text = "${readingSpeed.toInt()} pages / minute"

        var estimatedTime = 0F
        if (readingSpeed > 0) {
            estimatedTime = (book.totalPages - book.pagesRead) / readingSpeed.toInt().toFloat()
        }
        tvEstimatedTime.text = "${estimatedTime.toInt()} minutes left"
    }

    private fun toggleRead() {
        if (isTracking) {
            viewModel.pauseTimer()
        } else {
            viewModel.startTimer()
        }
    }

    private fun subscribeToObservers() {
        viewModel.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        viewModel.timeReadInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInSeconds = it / 1000
            tvTimer.text = BookTrackingUtility.getFormattedStopwatchTime(it)
        })
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            btnToggle.text = "Start"
            btnDone.visibility = View.VISIBLE
        } else {
            btnToggle.text = "Pause"
            btnDone.visibility = View.GONE
        }
    }

    private fun showPagesReadDialogAndUpdate(book: Book) {
        val dialog = MaterialDialog(requireContext())
            .noAutoDismiss()
            .customView(R.layout.layout_save_pages_read)

        dialog.findViewById<NumberPicker>(R.id.npPagesRead).wrapSelectorWheel = false
        dialog.findViewById<NumberPicker>(R.id.npPagesRead).minValue = 0
        dialog.findViewById<NumberPicker>(R.id.npPagesRead).maxValue = book.totalPages
        dialog.findViewById<NumberPicker>(R.id.npPagesRead).value = book.pagesRead

        dialog.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<NumberPicker>(R.id.npPagesRead)
            .setOnValueChangedListener { _, _, newVal ->
                if (newVal == book.totalPages) {
                    dialog.findViewById<TextView>(R.id.tvSave).text = "Finish"
                    dialog.findViewById<TextInputLayout>(R.id.etReview).visibility = View.VISIBLE
                } else {
                    dialog.findViewById<TextView>(R.id.tvSave).text = "Save"
                    dialog.findViewById<TextInputLayout>(R.id.etReview).visibility = View.GONE
                }
            }

        dialog.findViewById<TextView>(R.id.tvSave).setOnClickListener {

            val enteredLastPage =
                dialog.getCustomView().findViewById<NumberPicker>(R.id.npPagesRead).value

            book.id?.let { id ->
                viewModel.updatePagesReadAndTimeRead(
                    enteredLastPage,
                    currentTimeInSeconds,
                    id
                )
            }

            timelineViewModel.updateOrInsertDayStats(
                DayStats(
                    ZonedDateTime.now(),
                    Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
                    enteredLastPage - book.pagesRead
                )
            )

            if (dialog.findViewById<TextView>(R.id.tvSave).text == "Save") {
                dialog.dismiss()
                findNavController().navigate(R.id.action_trackingFragment_to_readingFragment)
            } else {
                val review =
                    dialog.findViewById<TextInputLayout>(R.id.etReview).editText?.text.toString()
                val dateTimestamp = Calendar.getInstance().timeInMillis
                viewModel.updateFinishedReviewAndTimestamp(review, dateTimestamp, book.id!!)

                dialog.dismiss()
                findNavController().navigate(R.id.action_trackingFragment_to_finishedFragment)
            }
        }

        dialog.show()
    }
}