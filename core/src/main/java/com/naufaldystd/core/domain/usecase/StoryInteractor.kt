package com.naufaldystd.core.domain.usecase

import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.data.source.StoryRepositoryImpl
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * Implementation of use case, using the function from Repository Implementation for cleaner flow.
 *
 * @property storyRepositoryImpl
 * @constructor Create empty Story interactor
 */
class StoryInteractor @Inject constructor(private val storyRepositoryImpl: StoryRepositoryImpl) :
	StoryUseCase {
	override suspend fun registerAccount(
		name: String,
		email: String,
		password: String
	): Flow<Resource<String>> = storyRepositoryImpl.registerAccount(name, email, password)

	override suspend fun loginAccount(email: String, password: String): Flow<Resource<UserModel>> =
		storyRepositoryImpl.loginAccount(email, password)

	override fun getAllStories(token: String): Flow<Resource<List<Story>>> =
		storyRepositoryImpl.getAllStories(token)

	override suspend fun addStory(
		token: String,
		description: RequestBody,
		photo: MultipartBody.Part
	): Flow<Resource<String>> = storyRepositoryImpl.addStory(token, description, photo)

	override suspend fun addStoryGuest(
		description: RequestBody,
		photo: MultipartBody.Part
	): Flow<Resource<String>> = storyRepositoryImpl.addStoryGuest(description, photo)
}