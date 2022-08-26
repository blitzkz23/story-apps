package com.naufaldystd.storyapps.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.ActivityMainBinding
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

		setupViewModel()
	}

	private fun setupViewModel() {
		mainViewModel.getUser().observe(this) { user ->
			if (user.isLogin) findNavController(R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_storyFragment)
		}
	}
}