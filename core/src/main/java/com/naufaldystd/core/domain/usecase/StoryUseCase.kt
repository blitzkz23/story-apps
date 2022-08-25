package com.naufaldystd.core.domain.usecase

import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface StoryUseCase {
	suspend fun registerAccount(name: String, email: String, password: String): Resource<String>
	suspend fun loginAccount(email: String, password: String): Resource<UserModel>
	fun getAllStories(token: String): Flow<Resource<List<Story>>>
}