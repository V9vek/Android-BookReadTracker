package com.viveksharma.bookreadtracker.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.viveksharma.bookreadtracker.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.navHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        bottomNavigationView.setupWithNavController(navController)

        navHostFragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.addBookFragment -> {
                        bottomNavigationView.visibility = View.GONE
                        toolbar_title.text = "Add Book"
                    }
                    R.id.trackingFragment -> {
                        bottomNavigationView.visibility = View.GONE
                        toolbar_title.text = "Track Book"
                    }
                    R.id.finishedFragment -> {
                        bottomNavigationView.visibility = View.VISIBLE
                        toolbar_title.text = "Finished Books"
                    }
                    R.id.readingFragment -> {
                        bottomNavigationView.visibility = View.VISIBLE
                        toolbar_title.text = "Currently Reading"
                    }
                    R.id.timelineFragment -> {
                        bottomNavigationView.visibility = View.VISIBLE
                        toolbar_title.text = "Timeline"
                    }
                }
            }
    }
}