package com.naufaldystd.storyapps.ui.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.ActivitySettingBinding
import com.naufaldystd.storyapps.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
	private val settingViewModel: SettingViewModel by viewModels()

	private val binding: ActivitySettingBinding by lazy {
		ActivitySettingBinding.inflate(layoutInflater)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		setupFullscreen()
		setupUserInfo()

		findViewById<ImageButton>(R.id.btn_back)?.setOnClickListener {
			onBackPressed()
		}
		binding.btnLogout.setOnClickListener {
			settingViewModel.logOutUser()
			val intent = Intent(this@SettingActivity, MainActivity::class.java)
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(intent)
			finish()
		}
	}

	private fun setupUserInfo() {
		settingViewModel.getUser().observe(this) { user ->
			binding.userName.text = user.name
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
}