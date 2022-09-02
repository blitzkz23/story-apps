package com.naufaldystd.storyapps.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.storyapps.databinding.ActivityAddStoryBinding
import com.naufaldystd.storyapps.utils.rotateBitmap
import com.naufaldystd.storyapps.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {
	private val binding: ActivityAddStoryBinding by lazy {
		ActivityAddStoryBinding.inflate(layoutInflater)
	}
	private val addStoryViewModel: AddStoryViewModel by viewModels()
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

		binding.inputDescription.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				setButtonEnable()
			}

			override fun afterTextChanged(s: Editable?) {}
		})
	}

	private fun setupButtonAction() {
		with(binding) {
			btnCamerax.setOnClickListener { startCameraX() }
			btnGallery.setOnClickListener { startGallery() }
			btnUpload.setOnClickListener { uploadStory() }
		}
	}

	/**
	 * Set validation for button state, only enable button if form are not empty and formats are correct
	 */
	private fun setButtonEnable() {
		val description = binding.inputDescription.text
		binding.btnUpload.isEnabled =
			(description != null) && description.toString().isNotEmpty() && getFile != null
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
		if (getFile != null) {
			val file = reduceFileImage(getFile as File)

			val description =
				binding.inputDescription.text.toString().toRequestBody("text/plain".toMediaType())
			val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
			val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
				"photo",
				file.name,
				requestImageFile
			)

			binding.loading.visibility = View.VISIBLE
			addStoryViewModel.getUser().observe(this) { user ->
				lifecycleScope.launch {
					addStoryViewModel.addStory(user.token, description, imageMultipart)
						.observe(this@AddStoryActivity) { respond ->
							when (respond) {

								is Resource.Success -> {
									binding.loading.visibility = View.GONE
									Toast.makeText(
										this@AddStoryActivity,
										"Berhasil mengupload kisah",
										Toast.LENGTH_SHORT
									).show()
								}
								else -> {
									binding.loading.visibility = View.GONE
									Toast.makeText(
										this@AddStoryActivity,
										"Gagal menunggah kisah, silahkan coba lagi",
										Toast.LENGTH_SHORT
									).show()
								}
							}
						}
				}
			}
		} else {
			Toast.makeText(
				this@AddStoryActivity,
				"Silahkan masukkan berkas gambar terlebih dahulu.",
				Toast.LENGTH_SHORT
			).show()
		}
	}

	private fun reduceFileImage(file: File): File {
		val bitmap = BitmapFactory.decodeFile(file.path)
		var compressQuality = 100
		var streamLength: Int
		do {
			val bmpStream = ByteArrayOutputStream()
			bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
			val bmpPicByteArray = bmpStream.toByteArray()
			streamLength = bmpPicByteArray.size
			compressQuality -= 5
		} while (streamLength > 1000000)
		bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
		return file
	}


	/**
	 * Set full screen without default action bar
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