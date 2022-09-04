package com.naufaldystd.storyapps.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.widget.TextView
import com.naufaldystd.storyapps.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Rotate bitmap of camera X result
 *
 * @param bitmap
 * @param isBackCamera
 * @return
 */
fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
	val matrix = Matrix()
	return if (isBackCamera) {
		matrix.postRotate(90f)
		Bitmap.createBitmap(
			bitmap,
			0,
			0,
			bitmap.width,
			bitmap.height,
			matrix,
			true
		)
	} else {
		matrix.postRotate(-90f)
		matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
		Bitmap.createBitmap(
			bitmap,
			0,
			0,
			bitmap.width,
			bitmap.height,
			matrix,
			true
		)
	}
}

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
	FILENAME_FORMAT,
	Locale.US
).format(System.currentTimeMillis())

/**
 * Create custom temp file
 *
 * @param context
 * @return
 */
fun createCustomTempFile(context: Context): File {
	val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
	return File.createTempFile(timeStamp, ".jpg", storageDir)
}

/**
 * Create file
 *
 * @param application
 * @return
 */
fun createFile(application: Application): File {
	val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
		File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
	}

	val outputDirectory = if (
		mediaDir != null && mediaDir.exists()
	) mediaDir else application.filesDir

	return File(outputDirectory, "$timeStamp.jpg")
}

/**
 * Uri to file
 *
 * @param selectedImg
 * @param context
 * @return
 */
fun uriToFile(selectedImg: Uri, context: Context): File {
	val contentResolver: ContentResolver = context.contentResolver
	val myFile = createCustomTempFile(context)

	val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
	val outputStream: OutputStream = FileOutputStream(myFile)
	val buf = ByteArray(1024)
	var len: Int
	while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
	outputStream.close()
	inputStream.close()

	return myFile
}

/**
 * Set TextView text attribute to locale date format
 *
 * @param datetime Timestamp
 */
fun TextView.setLocalDateFormat(datetime: String) {
	val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
	val date = sdf.parse(datetime) as Date

	val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date)
	this.text = formattedDate
}