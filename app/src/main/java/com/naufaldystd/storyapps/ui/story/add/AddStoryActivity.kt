package com.naufaldystd.storyapps.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.ActivityAddStoryBinding
import com.naufaldystd.storyapps.utils.rotateBitmap
import com.naufaldystd.storyapps.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
	private var location: Location? = null
	private lateinit var fusedLocationClient: FusedLocationProviderClient

	companion object {
		const val CAMERA_X_RESULT = 200

		private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
		private const val REQUEST_CODE_PERMISSIONS = 10
	}

	/**
	 * Override to check if permissions are granted
	 *
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
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
					getString(R.string.no_permission),
					Toast.LENGTH_SHORT
				).show()
				finish()
			}
		}
	}

	/**
	 * Check permissions
	 */
	private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
		ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
		if (!allPermissionsGranted()) {
			ActivityCompat.requestPermissions(
				this,
				REQUIRED_PERMISSIONS,
				REQUEST_CODE_PERMISSIONS
			)
		}

		setupFullscreen()
		setupButtonAction()
		binding.checkBoxShareloc.setOnCheckedChangeListener { _, isChecked ->
			if (isChecked) getLastLocation() else this.location = null
		}
		binding.inputDescription.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				setButtonEnable()
			}

			override fun afterTextChanged(s: Editable?) {}
		})
	}

	/**
	 * Launch request permission
	 */
	private val requestPermissionLauncher =
		registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
			when {
				permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
					// Precise location access granted
					getLastLocation()
				}
				permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
					// Only approximate location access granted
					getLastLocation()
				}
				else -> {
					// No location access granted
				}
			}
		}

	/**
	 * Check if permission granted
	 *
	 * @param permission
	 * @return
	 */
	private fun checkPermission(permission: String): Boolean {
		return ContextCompat.checkSelfPermission(
			this, permission
		) == PackageManager.PERMISSION_GRANTED
	}

	/**
	 * Get last location of the device
	 *
	 */
	private fun getLastLocation() {
		if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
			fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
				if (location != null) {
					this.location = location
				} else {
					Toast.makeText(
						this,
						getString(R.string.loc_not_found),
						Toast.LENGTH_SHORT
					).show()
				}
			}
		} else {
			requestPermissionLauncher.launch(
				arrayOf(
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION
				)
			)
		}
	}

	/**
	 * Set click listener for all button
	 *
	 */
	private fun setupButtonAction() {
		findViewById<ImageButton>(R.id.btn_back)?.setOnClickListener {
			onBackPressed()
		}
		with(binding) {
			btnCamerax.setOnClickListener { startCameraX() }
			btnGallery.setOnClickListener { startGallery() }
			btnUpload.setOnClickListener { uploadStory() }
		}
	}

	/**
	 * Set validation for button state, only enable button if form are not empty and formats are correct
	 *
	 */
	private fun setButtonEnable() {
		val description = binding.inputDescription.text
		binding.btnUpload.isEnabled =
			(description != null) && description.toString().isNotEmpty() && getFile != null
	}

	/**
	 * Start CameraX to take a picture
	 *
	 */
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

	/**
	 * Get image from gallery
	 *
	 */
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

	/**
	 * Upload request image, description to API to create new story
	 *
	 */
	private fun uploadStory() {
		var lat: RequestBody? = null
		var lon: RequestBody? = null
		if (location != null) {
			lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
			lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
		}

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
			lifecycleScope.launch {
				addStoryViewModel.getUser().collect { user ->
					if (user.name == getString(R.string.tamu) || user.name == getString(R.string.guest)) {
						addStoryGuest(description, imageMultipart, lat, lon)
					} else {
						addStoryUser(user.token, description, imageMultipart, lat, lon)
					}
				}
			}
		} else {
			Toast.makeText(
				this@AddStoryActivity,
				getString(R.string.no_image),
				Toast.LENGTH_SHORT
			).show()
		}
	}

	/**
	 * Upload new story function for logged in user
	 *
	 * @param token
	 * @param description
	 * @param imageMultipart
	 * @param lat
	 * @param lon
	 */
	private fun addStoryUser(
		token: String,
		description: RequestBody,
		imageMultipart: MultipartBody.Part,
		lat: RequestBody? = null,
		lon: RequestBody? = null
	) {
		lifecycleScope.launch {
			addStoryViewModel.addStory(token, description, imageMultipart, lat, lon)
				.collect { respond ->
					when (respond) {
						is Resource.Success -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								this@AddStoryActivity,
								getString(R.string.upload_success),
								Toast.LENGTH_SHORT
							).show()
							finish()
						}
						else -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								this@AddStoryActivity,
								getString(R.string.upload_failed),
								Toast.LENGTH_SHORT
							).show()
						}
					}
				}
		}
	}

	/**
	 * Upload new story function for guest
	 *
	 * @param description
	 * @param imageMultipart
	 * @param lat
	 * @param lon
	 */
	private fun addStoryGuest(
		description: RequestBody,
		imageMultipart: MultipartBody.Part,
		lat: RequestBody? = null,
		lon: RequestBody? = null
	) {
		lifecycleScope.launch {
			addStoryViewModel.addStoryGuest(description, imageMultipart, lat, lon)
				.collect { respond ->
					when (respond) {
						is Resource.Success -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								this@AddStoryActivity,
								getString(R.string.upload_success),
								Toast.LENGTH_SHORT
							).show()
							finish()
						}
						else -> {
							binding.loading.visibility = View.GONE
							Toast.makeText(
								this@AddStoryActivity,
								getString(R.string.upload_failed),
								Toast.LENGTH_SHORT
							).show()
						}
					}
				}
		}
	}

	/**
	 * Reduce file size by compressing bitmap to lesser than 1 Megabyte
	 *
	 * @param file
	 * @return
	 */
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