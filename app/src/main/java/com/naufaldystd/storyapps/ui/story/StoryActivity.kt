package com.naufaldystd.storyapps.ui.story

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.ui.StoryAdapter
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentStoryBinding
import com.naufaldystd.storyapps.ui.detail.DetailStoryActivity
import com.naufaldystd.storyapps.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryActivity : AppCompatActivity() {
	private val binding: FragmentStoryBinding by lazy {
		FragmentStoryBinding.inflate(layoutInflater)
	}
	private val storyViewModel: StoryViewModel by viewModels()
	private lateinit var storyAdapter: StoryAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		setupFullscreen()

		storyAdapter = StoryAdapter()
		storyAdapter.onItemClick = { intentData ->
			val intent = Intent(this@StoryActivity, DetailStoryActivity::class.java)
			intent.putExtra(DetailStoryActivity.EXTRA_PARCEL, intentData)
			startActivity(intent)
		}

		storyViewModel.getUser().observe(this) { user ->
			if (user.name != getString(R.string.guest)) {
				setupHeaderTokenAndStoryData()
			} else {
				with(binding) {
					messageForGuest.visibility = View.VISIBLE
					btnRegister2.visibility = View.VISIBLE
					rvStory.visibility = View.GONE
				}
			}
		}

		findViewById<ImageButton>(R.id.btn_setting)?.setOnClickListener {
			val intent = Intent(this@StoryActivity, SettingActivity::class.java)
			startActivity(intent)
		}
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

	private fun setupHeaderTokenAndStoryData() {
		storyViewModel.getUser().observe(this) { user ->
			storyViewModel.getAllStories(user.token).observe(this) { story ->
				if (story != null) {
					when (story) {
						is Resource.Loading -> binding.loading.visibility = View.GONE
						is Resource.Success -> {
							binding.loading.visibility = View.GONE
							storyAdapter.setData(story.data)
						}
						is Resource.Error -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								this,
								"Gagal menampilkan data, mohon coba lagi nanti",
								Toast.LENGTH_SHORT
							).show()
						}
					}
				}
			}
		}
		binding.rvStory.visibility = View.VISIBLE
		with(binding.rvStory) {
			layoutManager = LinearLayoutManager(context)
			setHasFixedSize(true)
			adapter = storyAdapter
		}
	}
}