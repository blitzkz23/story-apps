package com.naufaldystd.storyapps.ui.story.add

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import com.naufaldystd.storyapps.databinding.ActivityCameraXactivityBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraXActivity : AppCompatActivity() {
	private val binding: ActivityCameraXactivityBinding by lazy {
		ActivityCameraXactivityBinding.inflate(layoutInflater)
	}
	private lateinit var cameraExecutor: ExecutorService

	private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
	private var imageCapture: ImageCapture? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		cameraExecutor = Executors.newSingleThreadExecutor()
		setupButtonAction()
	}

	public override fun onResume() {
		super.onResume()
		hideSystemUI()
		startCamera()
	}

	override fun onDestroy() {
		super.onDestroy()
		cameraExecutor.shutdown()
	}

	private fun setupButtonAction() {
		with(binding) {
			captureImage.setOnClickListener { takePhoto() }
			switchCamera.setOnClickListener {
				cameraSelector =
					if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
					else CameraSelector.DEFAULT_BACK_CAMERA
				startCamera()
			}
		}
	}

	private fun takePhoto() {

	}

	private fun startCamera() {

	}

	private fun hideSystemUI() {
		@Suppress("DEPRECATION")
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