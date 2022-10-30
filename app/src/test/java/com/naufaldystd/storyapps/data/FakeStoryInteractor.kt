package com.naufaldystd.storyapps.data

import androidx.paging.PagingData
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.usecase.StoryUseCase
import com.naufaldystd.storyapps.util.DataDummy.generateDummyStories
import com.naufaldystd.storyapps.util.DataDummy.generateDummyStoriesWithLocation
import com.naufaldystd.storyapps.util.DataDummy.generateDummySuccessMessage
import com.naufaldystd.storyapps.util.DataDummy.generateDummyUserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeStoryInteractor : StoryUseCase {
	private val dummyUser = generateDummyUserModel()
	private val dummyStories = generateDummyStories()
	private val dummyStoriesWithLoc = generateDummyStoriesWithLocation()
	private val dummySuccessMsg = generateDummySuccessMessage()

	override suspend fun registerAccount(
		name: String,
		email: String,
		password: String
	): Flow<Resource<String>> {
		return flow {
			Resource.Success(dummyUser)
		}
	}

	override suspend fun loginAccount(email: String, password: String): Flow<Resource<UserModel>> {
		return flow {
			Resource.Success(dummyUser)
		}
	}

	override fun getAllStories(token: String, location: Int?): Flow<PagingData<StoryResponse>> {
		return flow {
			Resource.Success(dummyStories)
		}
	}

	override fun getStoriesWithLocation(token: String): Flow<Resource<List<Story>>> {
		return flow {
			Resource.Success(dummyStoriesWithLoc)
		}
	}

	override suspend fun addStory(
		token: String,
		description: RequestBody,
		photo: MultipartBody.Part,
		lat: RequestBody?,
		lon: RequestBody?
	): Flow<Resource<String>> {
		return flow {
			Resource.Success(dummySuccessMsg)
		}
	}

	override suspend fun addStoryGuest(
		description: RequestBody,
		photo: MultipartBody.Part,
		lat: RequestBody?,
		lon: RequestBody?
	): Flow<Resource<String>> {
		return flow {
			Resource.Success(dummySuccessMsg)
		}
	}

}