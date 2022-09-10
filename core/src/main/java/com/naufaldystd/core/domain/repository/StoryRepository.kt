package com.naufaldystd.core.domain.repository

import androidx.paging.PagingData
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Story repository interface, will be implemented on data layer
 *
 * @constructor Create empty Story repository
 */
interface StoryRepository {
	suspend fun registerAccount(
		name: String,
		email: String,
		password: String
	): Flow<Resource<String>>

	suspend fun loginAccount(email: String, password: String): Flow<Resource<UserModel>>

	fun getAllStories(token: String, location: Int? = null): Flow<PagingData<StoryResponse>>

	suspend fun addStory(
		token: String,
		description: RequestBody,
		photo: MultipartBody.Part,
		lat: RequestBody? = null,
		lon: RequestBody? = null
	): Flow<Resource<String>>

	suspend fun addStoryGuest(
		description: RequestBody,
		photo: MultipartBody.Part,
		lat: RequestBody? = null,
		lon: RequestBody? = null
	): Flow<Resource<String>>
}