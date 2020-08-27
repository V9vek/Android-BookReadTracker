package com.viveksharma.bookreadtracker.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.viveksharma.bookreadtracker.R
import com.viveksharma.bookreadtracker.db.DayStats
import com.viveksharma.bookreadtracker.other.BookTrackingUtility
import com.viveksharma.bookreadtracker.ui.viewmodels.TimelineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_timeline.*
import org.threeten.bp.format.TextStyle
import java.util.*

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class TimelineFragment : Fragment(R.layout.fragment_timeline) {

    private val timelineViewModel: TimelineViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBookInformation()
        subscribeToObservers()
    }

    private fun setUpBookInformation() {
        timelineViewModel.readingBooksSortedByPriority.observe(viewLifecycleOwner, Observer {
            it?.let {
                tvInfo1.text = "You are reading ${it.size} book(s)"
            }
        })
        timelineViewModel.finishedBooksSortedByDate.observe(viewLifecycleOwner, Observer {
            it?.let {
                tvInfo2.text = "You have finished reading ${it.size} book(s)"
            }
        })
    }

    private fun subscribeToObservers() {
        timelineViewModel.allDayStats.observe(viewLifecycleOwner, Observer {
            val entries = arrayListOf<BarEntry>()
            val labels = arrayListOf<String>()
            it?.let {
                if (it.isEmpty()) {
                    showChart(false)
                } else {
                    showChart(true)
                }

                it.forEachIndexed { index, dayStats ->
                    val entry = BarEntry(index.toFloat(), dayStats.totalPagesRead.toFloat())
                    val label = dayStats.dayID.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
                    entries.add(entry)
                    labels.add(label)
                }

                setUpChart(entries, labels, it)
            }
        })
    }

    private fun showChart(show: Boolean) {
        tvNoChart.isVisible = !show
        barChart.isVisible = show
    }

    private fun setUpChart(
        entries: ArrayList<BarEntry>,
        labels: ArrayList<String>,
        it: List<DayStats>
    ) {
        val textColor = ContextCompat.getColor(requireContext(), R.color.colorWhite)
        val barColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)

        val dataSetValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return BookTrackingUtility.getTotalPagesReadString(value)
            }
        }
        val xAxisValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labels[value.toInt()]
            }
        }

        val barDataSet = BarDataSet(entries, "# of Pages Read").apply {
            valueTextColor = textColor
            color = barColor
            valueFormatter = dataSetValueFormatter
        }

        barChart.xAxis.valueFormatter = xAxisValueFormatter
        barChart.xAxis.granularity = 1f
        barChart.xAxis.textColor = textColor
        barChart.legend.textColor = textColor
        barChart.description.isEnabled = false
        barChart.isDoubleTapToZoomEnabled = false
        barChart.axisLeft.isEnabled = false
        barChart.axisRight.isEnabled = false

        barChart.data = BarData(barDataSet)
        barChart.setDrawGridBackground(false)
        barChart.invalidate()
    }
}