package com.naufaldystd.storyapps.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.naufaldystd.storyapps.databinding.ActivityAddStoryBinding
import com.naufaldystd.storyapps.utils.rotateBitmap
import com.naufaldystd.storyapps.utils.uriToFile
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
		setupButtonAction()
	}

	private fun setupButtonAction() {
		with(binding) {
			btnCamerax.setOnClickListener { startCameraX() }
			btnGallery.setOnClickListener { startGallery() }
			btnUpload.setOnClickListener { uploadStory() }
		}
	}

	private fun startCameraX() {
		val intent = Intent(this, CameraXActivity::class.java)
		launcherIntentCameraX.launch(intent)
	}

	private val launcherIntentCameraX = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) {
		if (it.resultCode == CAMERA_X_RESULT) {
			val myFile = it.data?.getSerializableExtra("picture") as File
			val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

			getFile = myFile
			val result = rotateBitmap(
				BitmapFactory.decodeFile(myFile.path),
				isBackCamera
			)
			binding.ivPreview.setImageBitmap(result)
		}
	}

	private fun startGallery() {
		val intent = Intent()
		intent.action = ACTION_GET_CONTENT
		intent.type = "image/*"
		val chooser = Intent.createChooser(intent, "Choose a picture")
		launcherIntentGallery.launch(chooser)
	}

	private val launcherIntentGallery = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result ->
		if (result.resultCode == RESULT_OK) {
			val selectedImg: Uri = result.data?.data as Uri
			val myFile = uriToFile(selectedImg, this@AddStoryActivity)

			getFile = myFile
			binding.ivPreview.setImageURI(selectedImg)
		}
	}

	private fun uploadStory() {
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