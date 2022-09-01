package com.naufaldystd.storyapps.ui.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.utils.Constants.EXTRA
import com.naufaldystd.storyapps.databinding.ActivityDetailStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailStoryActivity : AppCompatActivity() {
	private val binding: ActivityDetailStoryBinding by lazy {
		ActivityDetailStoryBinding.inflate(layoutInflater)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		setupFullscreen()
		val data = intent.getParcelableExtra<Story>(EXTRA_PARCEL)
		if (data != null) {
			Log.d("Cobaintent", data.description)
			populateView(data)
		}
	}

	private fun populateView(data: Story) {

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

	companion object {
		const val EXTRA_PARCEL = EXTRA
	}
}