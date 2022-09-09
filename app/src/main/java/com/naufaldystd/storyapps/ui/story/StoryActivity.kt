package com.naufaldystd.storyapps.ui.story

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.ActivityStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryActivity : AppCompatActivity() {
	private val binding: ActivityStoryBinding by lazy {
		ActivityStoryBinding.inflate(layoutInflater)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		setupFullscreen()
		setupBottomNav()
	}

	/**
	 * Setup bottom navigation controller
	 *
	 */
	private fun setupBottomNav() {
		val bottomNav: BottomNavigationView = binding.bottomNavView
		val navHostFragment =
			supportFragmentManager.findFragmentById(R.id.nav_host_fragment_story) as NavHostFragment
		val navController = navHostFragment.navController
		// In order for this to work, the ids in the nav_graph must be the same as the one in the menu
		bottomNav.setupWithNavController(navController)
	}

	/**
	 * Set full screen without default action bar
	 *
	 */
	@Suppress("DEPRECATION")
	private fun setupFullscreen() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			window.insetsController?.hide(WindowInsets.Type.statusBars())
		} else {
			window.setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
			)
		}
		supportActionBar?.hide()
	}

}