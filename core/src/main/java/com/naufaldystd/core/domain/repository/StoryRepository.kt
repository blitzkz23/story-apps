package com.naufaldystd.core.domain.repository

import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
	suspend fun registerAccount(name: String, email: String, password: String): Resource<String>
	suspend fun loginAccount(email: String, password: String): Resource<UserModel>
	fun getAllStories(token: String): Flow<Resource<List<Story>>>
}