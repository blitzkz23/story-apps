package com.naufaldystd.core.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.naufaldystd.core.data.source.local.LocalDataSource
import com.naufaldystd.core.data.source.local.room.StoryDatabase
import com.naufaldystd.core.data.source.remote.RemoteDataSource
import com.naufaldystd.core.data.source.remote.StoryRemoteMediator
import com.naufaldystd.core.data.source.remote.network.ApiService
import com.naufaldystd.core.data.source.remote.network.StoryApiResponse
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.repository.StoryRepository
import com.naufaldystd.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Story repository implementation that extends interface from Story Repository interfaces, also connect to data from remote and local source.
 *
 * @property remoteDataSource
 * @property localDataSource
 * @constructor Create empty Story repository impl
 */
@Singleton
class StoryRepositoryImpl @Inject constructor(
	private val remoteDataSource: RemoteDataSource,
	private val localDataSource: LocalDataSource,
	private val storyDatabase: StoryDatabase,
	private val apiService: ApiService,
) : StoryRepository {
	private val pagingSourceFactory = { storyDatabase.storyDao().getAllStories() }

	/**
	 * Register account request that send request to remote data source and connect it to API endpoint.
	 *
	 * @param name
	 * @param email
	 * @param password
	 * @return
	 */
	override suspend fun registerAccount(
		name: String,
		email: String,
		password: String
	): Flow<Resource<String>> {
		return flow {
			when (val response =
				remoteDataSource.registerAccount(name, email, password).first()) {
				is StoryApiResponse.Success -> {
					emit(Resource.Success(response.data))
				}
				is StoryApiResponse.Error -> {
					emit(Resource.Error(response.errorMessage))
				}
				else -> {
					emit(Resource.Loading())
				}
			}
		}
	}

	/**
	 * Login account send request to remote data source and connect it to API endpoint.
	 *
	 * @param email
	 * @param password
	 * @return
	 */
	override suspend fun loginAccount(email: String, password: String): Flow<Resource<UserModel>> {
		return flow {
			when (val response = remoteDataSource.loginAccount(email, password).first()) {
				is StoryApiResponse.Success -> {
					// Get data from previous response and turn it into model
					val loginResponse = response.data
					val userModel = DataMapper.mapLoginResponsesToUserModel(loginResponse)
					emit(Resource.Success(userModel))
				}
				is StoryApiResponse.Error -> {
					emit(Resource.Error(response.errorMessage))
				}
				else -> {
					emit(Resource.Loading())
				}
			}
		}
	}

	/**
	 * Get all stories request to remote data source and connect it to API endpoint.
	 *
	 * @param token
	 * @return
	 */
	@OptIn(ExperimentalPagingApi::class)
	override fun getAllStories(
		token: String,
		location: Int?
	): Flow<PagingData<StoryResponse>> {
		return Pager(
			config = PagingConfig(
				pageSize = NETWORK_PAGE_SIZE,
			),
			remoteMediator = StoryRemoteMediator(
				storyDatabase,
				apiService,
				"Bearer $token"
			),
			pagingSourceFactory = pagingSourceFactory
		).flow
	}

	override fun getStoriesWithLocation(token: String): Flow<Resource<List<Story>>> = flow {
		when (val response = remoteDataSource.getAllStories(token, location = 1).first()) {
			is StoryApiResponse.Success -> {
				val story = DataMapper.mapResponsesToStory(response.data)
				emit(Resource.Success(story))
			}
			is StoryApiResponse.Error -> {
				emit(Resource.Error(response.errorMessage))
			}
			else -> {
				emit(Resource.Loading())
			}
		}
	}

	/**
	 * Add story send request to remote data source and connect it to API endpoint for logged in user.
	 *
	 * @param token
	 * @param description
	 * @param photo
	 * @return
	 */
	override suspend fun addStory(
		token: String,
		description: RequestBody,
		photo: MultipartBody.Part,
		lat: RequestBody?,
		lon: RequestBody?
	): Flow<Resource<String>> {
		return flow {
			when (val response =
				remoteDataSource.addStory(token, description, photo, lat = lat, lon = lon)
					.first()) {
				is StoryApiResponse.Success -> {
					emit(Resource.Success(response.data))
				}
				is StoryApiResponse.Error -> {
					emit(Resource.Error(response.errorMessage))
				}
				else -> {
					emit(Resource.Loading())
				}
			}
		}
	}

	/**
	 * Add story send request to remote data source and connect it to API endpoint for guest user.
	 *
	 * @param description
	 * @param photo
	 * @param lat
	 * @param lon
	 * @return
	 */
	override suspend fun addStoryGuest(
		description: RequestBody,
		photo: MultipartBody.Part,
		lat: RequestBody?,
		lon: RequestBody?
	): Flow<Resource<String>> {
		return flow {
			when (val response =
				remoteDataSource.addStoryGuest(description, photo, lat = lat, lon = lon).first()) {
				is StoryApiResponse.Success -> {
					emit(Resource.Success(response.data))
				}
				is StoryApiResponse.Error -> {
					emit(Resource.Error(response.errorMessage))
				}
				else -> {
					emit(Resource.Loading())
				}
			}
		}
	}

	companion object {
		private const val NETWORK_PAGE_SIZE = 10
	}

}