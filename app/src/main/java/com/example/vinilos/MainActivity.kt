package com.example.vinilos

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.vinilos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_collector, R.id.navigation_album, R.id.navigation_artist
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("NavDebug", "Destination Changed: ID=${destination.id}, Label=${destination.label}, Nav Name=${resources.getResourceEntryName(destination.id)}")
            Log.d("NavDebug", "BottomNav Selected ID: ${navView.selectedItemId}, Expected ID: ${R.id.navigation_album}")

            when (destination.id) {
                R.id.navigation_album_detail,
                R.id.navigation_collector_detail -> {
                    binding.navView.visibility = View.GONE
                }

                else -> {
                    binding.navView.visibility = View.VISIBLE
                }
            }
        }
    }
}