package com.naufaldystd.core.data.source.remote

import android.util.Log
import com.naufaldystd.core.data.source.remote.network.ApiService
import com.naufaldystd.core.data.source.remote.network.StoryApiResponse
import com.naufaldystd.core.data.source.remote.response.LoginResult
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.utils.Constants.CONSTANT_RDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source, connect API service from retrofit and repository.
 *
 * @property apiService
 * @constructor Create empty Remote data source
 */
@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

	/**
	 * Register account request by sending name, email, and password to API endpoint.
	 *
	 * @param name
	 * @param email
	 * @param password
	 * @return
	 */
	suspend fun registerAccount(
		name: String,
		email: String,
		password: String
	): Flow<StoryApiResponse<String>> {
		return flow {
			try {
				val response = apiService.registerAccount(name, email, password)
				if (!response.error) {
					emit(StoryApiResponse.Success(response.message))
				} else {
					emit(StoryApiResponse.Empty)
				}
			} catch (e: Exception) {
				emit(StoryApiResponse.Error(e.toString()))
				Log.e(REMOTE_DATA_SOURCE, e.toString())
			}
		}.flowOn(Dispatchers.IO)
	}

	/**
	 * Login account request by sending email, and password to API endpoint.
	 *
	 * @param email
	 * @param password
	 * @return
	 */
	suspend fun loginAccount(email: String, password: String): Flow<StoryApiResponse<LoginResult>> {
		// Use channel flow instead of flow, because flow doesn't allow emit() concurrently.
		return channelFlow {
			try {
				val response = apiService.loginAccount(email, password)
				if (!response.error) {
					send(StoryApiResponse.Success(response.loginResult))
				} else {
					send(StoryApiResponse.Empty)
				}
			} catch (e: Exception) {
				send(StoryApiResponse.Error(e.toString()))
				Log.e(REMOTE_DATA_SOURCE, e.toString())
			}
		}
	}

	/**
	 * Add new story request for logged in account.
	 *
	 * @param token
	 * @param description
	 * @param photo
	 * @return
	 */
	// Function to add story for registered account
	suspend fun addStory(
		token: String,
		description: RequestBody,
		photo: MultipartBody.Part
	): Flow<StoryApiResponse<String>> {
		return channelFlow {
			try {
				val response = apiService.addStory("Bearer $token", description, photo)
				if (!response.error) {
					send(StoryApiResponse.Success(response.message))
				} else {
					send(StoryApiResponse.Empty)
				}
			} catch (e: Exception) {
				send(StoryApiResponse.Error(e.toString()))
				Log.e(REMOTE_DATA_SOURCE, e.toString())
			}
		}
	}

	/**
	 * Add new story request for guest.
	 *
	 * @param description
	 * @param photo
	 * @return
	 */
	suspend fun addStoryGuest(
		description: RequestBody,
		photo: MultipartBody.Part
	): Flow<StoryApiResponse<String>> {
		return channelFlow {
			try {
				val response = apiService.addStoryGuest(description, photo)
				if (!response.error) {
					send(StoryApiResponse.Success(response.message))
				} else {
					send(StoryApiResponse.Empty)
				}
			} catch (e: Exception) {
				send(StoryApiResponse.Error(e.toString()))
				Log.e(REMOTE_DATA_SOURCE, e.toString())
			}
		}
	}

	/**
	 * Get all stories data from API.
	 *
	 * @param token
	 * @return
	 */
	suspend fun getAllStories(
		token: String,
		location: Int? = null
	): Flow<StoryApiResponse<List<StoryResponse>>> {
		return channelFlow {
			try {
				val response = apiService.getAllStories("Bearer $token", location = location)
				val dataArray = response.listStory
				if (dataArray.isNotEmpty()) {
					send(StoryApiResponse.Success(response.listStory))
				} else {
					send(StoryApiResponse.Empty)
				}
			} catch (e: Exception) {
				send(StoryApiResponse.Error(e.toString()))
				Log.e(REMOTE_DATA_SOURCE, e.toString())
			}
		}
	}

	companion object {
		private const val REMOTE_DATA_SOURCE = CONSTANT_RDS
	}
}