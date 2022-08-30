package com.naufaldystd.storyapps.ui.main

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
import com.naufaldystd.storyapps.ui.login.LoginFragment
import com.naufaldystd.storyapps.ui.story.StoryFragment
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
		setupViewModel()
	}

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

	private fun setupViewModel() {
		// If user session is active, replace login fragment with story fragment
		mainViewModel.getUser().observe(this) { user ->
			if (user.isLogin) {
				val ft = supportFragmentManager.beginTransaction()
				ft.replace(R.id.nav_host_fragment, StoryFragment())
				ft.commit()
			}
		}
	}
}