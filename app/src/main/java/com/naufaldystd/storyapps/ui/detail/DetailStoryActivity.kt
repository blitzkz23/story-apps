package com.naufaldystd.storyapps.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.naufaldystd.core.R
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.utils.Constants.EXTRA
import com.naufaldystd.storyapps.databinding.ActivityDetailStoryBinding
import com.naufaldystd.storyapps.utils.setLocalDateFormat
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
		val data = intent.getParcelableExtra<StoryResponse>(EXTRA_PARCEL)
		if (data != null) {
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
	private fun populateView(data: StoryResponse) {
		with(binding) {
			Glide.with(this@DetailStoryActivity)
				.load(data.photoUrl)
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
			tvDatetime.setLocalDateFormat(data.createdAt)
		}
		findViewById<TextView>(com.naufaldystd.storyapps.R.id.tv_toolbar_txt).text =
			buildString {
				append(getString(com.naufaldystd.storyapps.R.string.story))
				append(" ")
				append(data.name)
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