package com.naufaldystd.core.data.source

import com.naufaldystd.core.data.source.local.LocalDataSource
import com.naufaldystd.core.data.source.remote.RemoteDataSource
import com.naufaldystd.core.data.source.remote.network.StoryApiResponse
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.repository.StoryRepository
import com.naufaldystd.core.utils.AppExecutors
import com.naufaldystd.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(
	private val remoteDataSource: RemoteDataSource,
	private val localDataSource: LocalDataSource,
	private val appExecutors: AppExecutors
) : StoryRepository {
	override suspend fun registerAccount(
		name: String,
		email: String,
		password: String
	): Resource<String> {
		return when (val response =
			remoteDataSource.registerAccount(name, email, password).first()) {
			is StoryApiResponse.Success -> {
				Resource.Success(response.data)
			}
			is StoryApiResponse.Error -> {
				Resource.Error(response.errorMessage)
			}
			else -> {
				Resource.Loading()
			}
		}
	}

	override suspend fun loginAccount(email: String, password: String): Resource<UserModel> {
		return when (val response = remoteDataSource.loginAccount(email, password).first()) {
			is StoryApiResponse.Success -> {
				// Get data from previous response and turn it into model
				val loginResponse = response.data
				val userModel = DataMapper.mapLoginResponsesToUserModel(loginResponse)
				Resource.Success(userModel)
			}
			is StoryApiResponse.Error -> {
				Resource.Error(response.errorMessage)
			}
			else -> {
				Resource.Loading()
			}
		}
	}

	override fun getAllStories(token: String): Flow<Resource<List<Story>>> =
		object : NetworkBoundResource<List<Story>, List<StoryResponse>>() {
			override fun loadFromDB(): Flow<List<Story>> {
				return localDataSource.getAllStories().map {
					DataMapper.mapStoryEntitiesToStory(it)
				}
			}

			override fun shouldFetch(data: List<Story>?): Boolean =
				(data == null) || data.isEmpty()

			override suspend fun createCall(): Flow<StoryApiResponse<List<StoryResponse>>> =
				remoteDataSource.getStories(token)

			override suspend fun saveCallResult(data: List<StoryResponse>) {
				val stories = DataMapper.mapStoryResponsesToEntities(data)
				localDataSource.insertStories(stories)
			}
		}.asFlow()

}