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
import androidx.recyclerview.widget.LinearLayoutManager
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
	private val storyViewModel: StoryViewModel by viewModels()
	private lateinit var storyAdapter: StoryAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		setupFullscreen()
		setupAdapter()
		setupButtonAction()

		storyViewModel.getUser().observe(this) { user ->
			if (user.name == getString(R.string.tamu) || user.name == getString(R.string.guest)) {
				with(binding) {
					imageSorry.visibility = View.VISIBLE
					messageForGuest.visibility = View.VISIBLE
					btnRegister2.visibility = View.VISIBLE
					rvStory.visibility = View.GONE
				}
			} else {
				setupHeaderTokenAndStoryData()
			}
		}
	}

	/**
	 * Set click listener for all button
	 *
	 */
	private fun setupButtonAction() {
		findViewById<ImageButton>(R.id.btn_setting)?.setOnClickListener {
			startActivity(Intent(this@StoryActivity, SettingActivity::class.java))
		}
		binding.btnAddStory.setOnClickListener {
			startActivity(Intent(this@StoryActivity, AddStoryActivity::class.java))
		}
		binding.btnRegister2.setOnClickListener {
			startActivity(Intent(this@StoryActivity, MainActivity::class.java).also { intent ->
				intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			})
			finish()
		}
	}

	/**
	 * Set adapter for recyclerview
	 *
	 */
	private fun setupAdapter() {
		storyAdapter = StoryAdapter()
		storyAdapter.onItemClick = { intentData ->
			val image = findViewById<ImageView>(com.naufaldystd.core.R.id.iv_item_image_list)
			val text = findViewById<TextView>(com.naufaldystd.core.R.id.tv_user_and_paragraph_list)
			val datetime = findViewById<TextView>(com.naufaldystd.core.R.id.tv_datetime_list)
			val optionsCompat: ActivityOptionsCompat =
				ActivityOptionsCompat.makeSceneTransitionAnimation(
					this@StoryActivity,
					Pair(image, "image"),
					Pair(text, "text"),
					Pair(datetime, "datetime")
				)
			val intent = Intent(this@StoryActivity, DetailStoryActivity::class.java)
			intent.putExtra(DetailStoryActivity.EXTRA_PARCEL, intentData)
			startActivity(intent, optionsCompat.toBundle())
		}
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

	/**
	 * Set header token for data request and set the returned data into adapter and eventually recyclerview
	 */
	private fun setupHeaderTokenAndStoryData() {
		binding.rvStory.visibility = View.VISIBLE
		binding.loading.visibility = View.VISIBLE
		storyViewModel.getUser().observe(this) { user ->
			storyViewModel.getAllStories(user.token).observe(this) { story ->
				if (story != null) {
					when (story) {
						is Resource.Loading -> binding.loading.visibility = View.VISIBLE
						is Resource.Success -> {
							binding.loading.visibility = View.GONE
							storyAdapter.setData(story.data)
						}
						is Resource.Error -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								this,
								getString(R.string.fail_load_data),
								Toast.LENGTH_SHORT
							).show()
							with(binding) {
								imageSorry.visibility = View.VISIBLE
								messageForGuest.visibility = View.VISIBLE
								messageForGuest.text = getString(R.string.data_not_available)
							}
						}
					}
				}
			}
		}
		with(binding.rvStory) {
			layoutManager = LinearLayoutManager(context)
			setHasFixedSize(true)
			adapter = storyAdapter
		}
	}
}