package com.naufaldystd.storyapps.ui.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.naufaldystd.core.R
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.utils.Constants.EXTRA
import com.naufaldystd.storyapps.databinding.ActivityDetailStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailStoryActivity : AppCompatActivity() {
	private val binding: ActivityDetailStoryBinding by lazy {
		ActivityDetailStoryBinding.inflate(layoutInflater)
	}

	companion object {
		const val EXTRA_PARCEL = EXTRA
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
		findViewById<ImageButton>(com.naufaldystd.storyapps.R.id.btn_back)?.setOnClickListener {
			onBackPressed()
		}
	}

	/**
	 * Populate view with data, if extra from previous page exists
	 *
	 * @param data
	 */
	private fun populateView(data: Story) {
		with(binding) {
			Glide.with(this@DetailStoryActivity)
				.load(data.photoURL)
				.apply(
					RequestOptions.placeholderOf(R.drawable.ic_loading)
						.error(R.drawable.ic_error)
				)
				.into(ivItemImage)
			tvUserAndParagraph.text = HtmlCompat.fromHtml(
				getString(
					R.string.story_text_format,
					data.name,
					data.description
				), HtmlCompat.FROM_HTML_MODE_LEGACY
			)
			tvDatetime.text = data.createdAt
		}
	}

	/**
	 * Setup fullscreen
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