package com.naufaldystd.core.domain.usecase

import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.data.source.StoryRepositoryImpl
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoryInteractor @Inject constructor(private val storyRepositoryImpl: StoryRepositoryImpl) :
	StoryUseCase {
	override suspend fun registerAccount(
		name: String,
		email: String,
		password: String
	): Resource<String> = storyRepositoryImpl.registerAccount(name, email, password)

	override suspend fun loginAccount(email: String, password: String): Resource<UserModel> =
		storyRepositoryImpl.loginAccount(email, password)

	override fun getAllStories(token: String): Flow<Resource<List<Story>>> =
		storyRepositoryImpl.getAllStories(token)
}