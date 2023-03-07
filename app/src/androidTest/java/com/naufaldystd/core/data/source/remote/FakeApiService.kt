package com.naufaldystd.core.data.source.remote

import com.naufaldystd.core.data.source.remote.network.ApiService
import com.naufaldystd.core.data.source.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService: ApiService {
	override suspend fun registerAccount(
		name: String,
		email: String,
		password: String
	): RegisterResponse {
		return null!!
	}

	override suspend fun loginAccount(email: String, password: String): LoginResponse {
		return null!!
	}

	override suspend fun addStory(
		token: String,
		description: RequestBody,
		file: MultipartBody.Part,
		lat: RequestBody?,
		lon: RequestBody?
	): AddStoryResponse {
		return null!!
	}

	override suspend fun addStoryGuest(
		description: RequestBody,
		file: MultipartBody.Part,
		lat: RequestBody?,
		lon: RequestBody?
	): AddStoryResponse {
		return null!!
	}

	override suspend fun getAllStories(
		token: String,
		page: Int?,
		size: Int?,
		location: Int?
	): ListStoryResponse {
		lateinit var listStory: ListStoryResponse
		val items: MutableList<StoryResponse> = mutableListOf()
		for (i in 0..100) {
			val story = StoryResponse(
				id = i.toString(),
				name = "name $i",
				description = "description $i",
				photoUrl = "photoUrl $i",
				createdAt = "createdAt $i",
				lat = 0.0,
				lon = 0.0
			)
			items.add(story)
		}
		val error = false
		val message = "success"
		listStory = ListStoryResponse(items, error, message)

		return listStory
	}
}