package com.naufaldystd.core.data.source.remote

import android.util.Log
import com.naufaldystd.core.data.source.remote.network.ApiService
import com.naufaldystd.core.data.source.remote.network.StoryApiResponse
import com.naufaldystd.core.data.source.remote.response.LoginResult
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.utils.Constants.CONSTANT_RDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

	// Register function with flow
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

	// Login function with flow
	@OptIn(ExperimentalCoroutinesApi::class)
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

	// Function to add story for registered account
	@OptIn(ExperimentalCoroutinesApi::class)
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

	// Function to add story for guest user
	@OptIn(ExperimentalCoroutinesApi::class)
	suspend fun addStoryGuest(
		description: RequestBody,
		photo: MultipartBody.Part
	): Flow<StoryApiResponse<String>> {
		return channelFlow{
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

	// function to get all stories from API for main page
	@OptIn(ExperimentalCoroutinesApi::class)
	suspend fun getStories(token: String): Flow<StoryApiResponse<List<StoryResponse>>> {
		return channelFlow {
			try {
				val response = apiService.getStories("Bearer $token")
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