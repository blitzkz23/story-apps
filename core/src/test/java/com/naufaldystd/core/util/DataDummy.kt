package com.naufaldystd.storyapps.util

import com.naufaldystd.core.data.source.remote.response.LoginResult
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.domain.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {
	fun generateDummyUserModel(): UserModel {
		return UserModel(
			name = "Naufal",
			userId = "nfl123",
			token = "eyJjhsdlajhdfalkjdasnjhfalkdjheo",
			isLogin = true
		)
	}

	fun generateDummyStories(): List<StoryResponse> {
		val items = arrayListOf<StoryResponse>()

		for (i in 0 until 20) {
			val item = StoryResponse(
				id = i.toString(),
				photoUrl = "xxx",
				createdAt = "2022-29-10T03:44:34.621Z",
				name = "Joko",
				description = "Joko photo",
				lat = 0.0,
				lon = 0.0
			)
			items.add(item)
		}

		return items
	}

	fun generateDummyStoriesWithLocation(): List<StoryResponse> {
		val items = arrayListOf<StoryResponse>()

		for (i in 0 until 20) {
			val item = StoryResponse(
				id = "str-$i",
				photoUrl = "photo-$i",
				createdAt = "2022-$i-10T03:44:34.621Z",
				name = "Joko",
				description = "Joko photo",
				lon = 20.00,
				lat = 320.00
			)
			items.add(item)
		}

		return items
	}

	fun generateDummyToken(): String {
		return "eyJjhsdlajhdfalkjdasnjhfalkdjheo"
	}

	fun generateDummyRegisterRequest(): AuthRequest {
		return AuthRequest(
			name = "Joko",
			email = "joko@gmail.com",
			password = "joksowi"
		)
	}

	fun generateDummyLoginRequest(): AuthRequest {
		return AuthRequest(
			email = "joko@gmail.com",
			password = "joksowi"
		)
	}

	fun generateDummyUserCreatedMessage(): String {
		return "User Created"
	}

	fun generateDummySuccessMessage(): String {
		return "success"
	}

	fun generateDummyErrorMessage(): String {
		return "error"
	}

	fun generateDummyStoryRequest(): StoryRequest {
		val dummyText = "sebuah text"
		val dummyDescription = dummyText.toRequestBody()
		val dummyPhoto = MultipartBody.Part.create(dummyText.toRequestBody())

		return StoryRequest(
			token = generateDummyToken(),
			description = dummyDescription,
			photo = dummyPhoto
		)
	}

	fun generateDummyLoginResult(): LoginResult {
		return LoginResult(
			name = "Joko",
			userId = "joko1",
			token = generateDummyToken()
		)
	}
}