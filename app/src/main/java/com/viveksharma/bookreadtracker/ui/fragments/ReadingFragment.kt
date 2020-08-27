package com.viveksharma.bookreadtracker.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.viveksharma.bookreadtracker.R
import com.viveksharma.bookreadtracker.adapters.ReadingBooksAdapter
import com.viveksharma.bookreadtracker.ui.viewmodels.ReadingViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_reading.*


@AndroidEntryPoint
class ReadingFragment : Fragment(R.layout.fragment_reading) {

    private val readingViewModel: ReadingViewModel by viewModels()

    private lateinit var readingBooksAdapter: ReadingBooksAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        readingBooksAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putParcelable("book", it)
            }
            findNavController().navigate(
                R.id.action_readingFragment_to_trackingFragment,
                bundle
            )
            readingViewModel.postInitialValuesOfTimer()
        }

        readingViewModel.readingBooksSortedByPriority.observe(viewLifecycleOwner, Observer {
            readingBooksAdapter.submitList(it)
            if (it.isNotEmpty()) {
                showBookIcon(false)
            } else {
                showBookIcon(true)
            }
        })

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_readingFragment_to_addBookFragment)
        }

        setupSwipeToDelete(view)
    }

    private fun showBookIcon(show: Boolean) {
        ivBooks.isVisible = show
        tvAddBooks.isVisible = show
    }

    private fun setUpRecyclerView() {
        rvReading.apply {
            readingBooksAdapter = ReadingBooksAdapter()
            adapter = readingBooksAdapter
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
                val book = readingBooksAdapter.differ.currentList[position]
                readingViewModel.deleteBook(book)

                Snackbar.make(view, "Deleting...", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        readingViewModel.insertBook(book)
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
            attachToRecyclerView(rvReading)
        }
    }
}