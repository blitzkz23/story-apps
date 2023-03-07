package com.naufaldystd.storyapps.util

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class StoryRequest(
	val token: String,
	val description: RequestBody,
	val photo: MultipartBody.Part,
	val lat: RequestBody? = null,
	val lon: RequestBody? = null
)
