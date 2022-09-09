package com.naufaldystd.storyapps.ui.story

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.ui.StoryAdapter
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.ActivityStoryBinding
import com.naufaldystd.storyapps.ui.detail.DetailStoryActivity
import com.naufaldystd.storyapps.ui.main.MainActivity
import com.naufaldystd.storyapps.ui.setting.SettingActivity
import com.naufaldystd.storyapps.ui.story.add.AddStoryActivity
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