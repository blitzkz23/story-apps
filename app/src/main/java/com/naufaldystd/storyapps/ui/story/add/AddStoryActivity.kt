package com.naufaldystd.storyapps.ui.story.add

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.naufaldystd.storyapps.databinding.ActivityAddStoryBinding
import java.io.File

class AddStoryActivity : AppCompatActivity() {
	private val binding: ActivityAddStoryBinding by lazy {
		ActivityAddStoryBinding.inflate(layoutInflater)
	}
	private var getFile: File? = null

	companion object {
		const val CAMERA_X_RESULT = 200

		private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
		private const val REQUEST_CODE_PERMISSIONS = 10
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == REQUEST_CODE_PERMISSIONS) {
			if (!allPermissionsGranted()) {
				Toast.makeText(
					this,
					"Tidak mendapatkan permission.",
					Toast.LENGTH_SHORT
				).show()
				finish()
			}
		}
	}

	private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
		ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		if (!allPermissionsGranted()) {
			ActivityCompat.requestPermissions(
				this,
				REQUIRED_PERMISSIONS,
				REQUEST_CODE_PERMISSIONS
			)
		}

		setupFullscreen()
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