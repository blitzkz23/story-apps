package com.naufaldystd.core.data.source.remote.network

import com.naufaldystd.core.data.source.remote.response.AddStoryResponse
import com.naufaldystd.core.data.source.remote.response.ListStoryResponse
import com.naufaldystd.core.data.source.remote.response.LoginResponse
import com.naufaldystd.core.data.source.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

	@FormUrlEncoded
	@POST("register")
	suspend fun registerAccount(
		@Field("name") name: String,
		@Field("email") email: String,
		@Field("password") password: String
	): RegisterResponse

	@FormUrlEncoded
	@POST("login")
	suspend fun loginAccount(
		@Field("email") email: String,
		@Field("password") password: String,
	): LoginResponse

	@Multipart
	@POST("stories")
	suspend fun addStory(
		@Part("description") description: RequestBody,
		@Part file: MultipartBody.Part
	): AddStoryResponse

	@Multipart
	@POST("stories/guest")
	suspend fun addStoryGuest(
		@Part("description") description: RequestBody,
		@Part file: MultipartBody.Part
	): AddStoryResponse


	@GET("stories")
	suspend fun getStories(
		@Header("Authorization") token: String
	): ListStoryResponse
}