package com.naufaldystd.core.data.source.remote.network

import com.naufaldystd.core.data.source.remote.response.AddStoryResponse
import com.naufaldystd.core.data.source.remote.response.ListStoryResponse
import com.naufaldystd.core.data.source.remote.response.LoginResponse
import com.naufaldystd.core.data.source.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

	@Multipart
	@POST("register")
	suspend fun registerAccount(
		@Part("name") name: RequestBody,
		@Part("email") email: RequestBody,
		@Part("password") password: RequestBody
	): RegisterResponse

	@Multipart
	@POST("login")
	suspend fun loginAccount(
		@Part("email") email: RequestBody,
		@Part("password") password: RequestBody,
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
	): ListStoryResponse
}