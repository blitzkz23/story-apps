package com.naufaldystd.storyapps.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.ActivityMainBinding
import com.naufaldystd.storyapps.ui.story.StoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	private val binding: ActivityMainBinding by lazy {
		ActivityMainBinding.inflate(layoutInflater)
	}
	private val mainViewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		setupFullscreen()
		checkLoggedUser()
	}

	/**
	 * Set full screen without default action bar
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

	/**
	 * Check if user session is active, redirect to main screen (story activity)
	 */
	private fun checkLoggedUser() {
		mainViewModel.getUser().observe(this) { user ->
			if (user.isLogin) {
				startActivity(Intent(this@MainActivity, StoryActivity::class.java).also { intent ->
					intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
				})
				finish()
			}
		}
	}
}