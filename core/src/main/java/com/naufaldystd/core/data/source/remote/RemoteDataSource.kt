package com.naufaldystd.core.data.source.remote

import android.os.Build.VERSION_CODES.P
import android.util.Log
import com.naufaldystd.core.data.source.local.entity.StoryEntity
import com.naufaldystd.core.data.source.remote.network.ApiService
import com.naufaldystd.core.data.source.remote.network.StoryApiResponse
import com.naufaldystd.core.data.source.remote.response.AddStoryResponse
import com.naufaldystd.core.data.source.remote.response.LoginResponse
import com.naufaldystd.core.data.source.remote.response.RegisterResponse
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.utils.Constants.CONSTANT_RDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

	// Register function with flow
	suspend fun registerAccount(name: String, email: String, password: String) : Flow<StoryApiResponse<RegisterResponse>> {
		return flow {
			try {
				val response = apiService.registerAccount(name, email, password)
				if (!response.error) {
					emit(StoryApiResponse.Success(response))
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
	suspend fun loginAccount(email: String, password: String) : Flow<StoryApiResponse<LoginResponse>> {
		return flow {
			try {
				val response = apiService.loginAccount(email, password)
				if (!response.error) {
					emit(StoryApiResponse.Success(response))
				} else {
					emit(StoryApiResponse.Empty)
				}
			} catch (e: Exception) {
				emit(StoryApiResponse.Error(e.toString()))
				Log.e(REMOTE_DATA_SOURCE, e.toString())
			}
		}
	}

	suspend fun addStory(description: RequestBody, photo: MultipartBody.Part): Flow<StoryApiResponse<AddStoryResponse>> {
		return flow {
			try {
				val response = apiService.addStory(description, photo)
				if (!response.error) {
					emit(StoryApiResponse.Success(response))
				} else {
					emit(StoryApiResponse.Empty)
				}
			} catch (e: Exception) {
				emit(StoryApiResponse.Error(e.toString()))
				Log.e(REMOTE_DATA_SOURCE, e.toString())
			}
		}
	}

	suspend fun addStoryGuest(description: RequestBody, photo: MultipartBody.Part): Flow<StoryApiResponse<AddStoryResponse>> {
		return flow {
			try {
				val response = apiService.addStoryGuest(description, photo)
				if (!response.error) {
					emit(StoryApiResponse.Success(response))
				} else {
					emit(StoryApiResponse.Empty)
				}
			} catch (e: Exception) {
				emit(StoryApiResponse.Error(e.toString()))
				Log.e(REMOTE_DATA_SOURCE, e.toString())
			}
		}
	}

	suspend fun getStories(token: String): Flow<StoryApiResponse<List<StoryResponse>>> {
		return flow {
			try {
				val response = apiService.getStories(token)
				val dataArray = response.listStory
				if (dataArray.isNotEmpty()) {
					emit(StoryApiResponse.Success(response.listStory))
				} else {
					emit(StoryApiResponse.Empty)
				}
			} catch (e: Exception) {
				emit(StoryApiResponse.Error(e.toString()))
				Log.e(REMOTE_DATA_SOURCE, e.toString())
			}
		}
	}

	companion object {
		private const val REMOTE_DATA_SOURCE = CONSTANT_RDS
	}
}