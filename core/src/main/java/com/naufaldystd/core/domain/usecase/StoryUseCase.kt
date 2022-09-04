package com.naufaldystd.core.domain.usecase

import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Story use case interface, will be implemented by Story Interactor and called by view model to connect data to the front end.
 *
 * @constructor Create empty Story use case
 */
interface StoryUseCase {
	suspend fun registerAccount(
		name: String,
		email: String,
		password: String
	): Flow<Resource<String>>

	suspend fun loginAccount(email: String, password: String): Flow<Resource<UserModel>>

	fun getAllStories(token: String): Flow<Resource<List<Story>>>

	suspend fun addStory(
		token: String,
		description: RequestBody,
		photo: MultipartBody.Part
	): Flow<Resource<String>>

	suspend fun addStoryGuest(
		description: RequestBody,
		photo: MultipartBody.Part
	): Flow<Resource<String>>
}