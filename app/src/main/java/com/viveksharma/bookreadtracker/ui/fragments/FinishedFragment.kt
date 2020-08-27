package com.viveksharma.bookreadtracker.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.viveksharma.bookreadtracker.R
import com.viveksharma.bookreadtracker.adapters.FinishedBooksAdapter
import com.viveksharma.bookreadtracker.ui.viewmodels.FinishedViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_finished.*
import kotlinx.android.synthetic.main.fragment_finished.ivBooks
import kotlinx.android.synthetic.main.fragment_finished.tvAddBooks
import kotlinx.android.synthetic.main.fragment_reading.*

@AndroidEntryPoint
class FinishedFragment : Fragment(R.layout.fragment_finished) {

    private val finishedViewModel: FinishedViewModel by viewModels()

    private lateinit var finishedBooksAdapter: FinishedBooksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        finishedViewModel.finishedBooksSortedByDate.observe(viewLifecycleOwner, Observer {
            finishedBooksAdapter.submitList(it)
            if (it.isNotEmpty()) {
                showBookIcon(false)
            } else {
                showBookIcon(true)
            }
        })

        setupSwipeToDelete(view)
    }

    private fun showBookIcon(show: Boolean) {
        ivBooks.isVisible = show
        tvAddBooks.isVisible = show
    }

    private fun setUpRecyclerView() {
        rvFinished.apply {
            finishedBooksAdapter = FinishedBooksAdapter()
            adapter = finishedBooksAdapter
            smoothScrollToPosition(0)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSwipeToDelete(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val book = finishedBooksAdapter.differ.currentList[position]
                finishedViewModel.deleteBook(book)

                Snackbar.make(view, "Deleting...", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        finishedViewModel.insertBook(book)
                    }
                }.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorHighPriority
                        )
                    ).addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvFinished)
        }
    }
}