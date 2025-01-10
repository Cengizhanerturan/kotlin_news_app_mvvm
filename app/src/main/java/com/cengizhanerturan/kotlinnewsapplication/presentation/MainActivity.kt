package com.cengizhanerturan.kotlinnewsapplication.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.core.theme.ThemeManager
import com.cengizhanerturan.kotlinnewsapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.applyTheme(this)
        installSplashScreen()
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupNavController()
    }


    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        bottomNavSetOnItemSelectedListener(navController)
        addOnDestinationChangedListener(navController)
        onBackPressedDispatcher(navController)
    }

    private fun bottomNavSetOnItemSelectedListener(navController: NavController) {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            if (navController.currentDestination?.id != menuItem.itemId) {
                navController.navigate(menuItem.itemId)
            }
            true
        }
    }

    private fun addOnDestinationChangedListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            applyWindowInsetsForDestination(destination.id)
            setBottomNavigationVisibility(destination.id)
        }
    }

    private fun applyWindowInsetsForDestination(destinationId: Int) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            when (destinationId) {
                R.id.newsDetailFragment -> {
                    v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                }

                R.id.searchFragment -> {
                    v.setPadding(
                        systemBars.left,
                        systemBars.top,
                        systemBars.right,
                        systemBars.bottom
                    )
                }

                else -> {
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
                }
            }
            insets
        }
    }

    private fun setBottomNavigationVisibility(destinationId: Int) {
        binding.bottomNavigationView.visibility = when (destinationId) {
            R.id.homeFragment, R.id.discoverFragment, R.id.savedFragment, R.id.settingsFragment -> View.VISIBLE
            else -> View.GONE
        }
    }


    private fun onBackPressedDispatcher(navController: NavController) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestination = navController.currentDestination?.id
                when (currentDestination) {
                    R.id.homeFragment -> {
                        finish()
                    }

                    in listOf(
                        R.id.discoverFragment,
                        R.id.savedFragment,
                        R.id.settingsFragment
                    ) -> {
                        navController.navigate(R.id.homeFragment)
                    }

                    else -> {
                        navController.popBackStack()
                    }
                }
            }
        })
    }
}