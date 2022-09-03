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

	/**
	 * Send register account request to API.
	 *
	 * @param name
	 * @param email
	 * @param password
	 * @return
	 */
	@FormUrlEncoded
	@POST("register")
	suspend fun registerAccount(
		@Field("name") name: String,
		@Field("email") email: String,
		@Field("password") password: String
	): RegisterResponse

	/**
	 * Send login account request to API.
	 *
	 * @param email
	 * @param password
	 * @return
	 */
	@FormUrlEncoded
	@POST("login")
	suspend fun loginAccount(
		@Field("email") email: String,
		@Field("password") password: String,
	): LoginResponse

	/**
	 * Send create new story request to API for logged in user.
	 *
	 * @param token
	 * @param description
	 * @param file
	 * @return
	 */
	@Multipart
	@POST("stories")
	suspend fun addStory(
		@Header("Authorization") token: String,
		@Part("description") description: RequestBody,
		@Part file: MultipartBody.Part
	): AddStoryResponse

	/**
	 * Send create new story request to API for guest user.
	 *
	 * @param description
	 * @param file
	 * @return
	 */
	@Multipart
	@POST("stories/guest")
	suspend fun addStoryGuest(
		@Part("description") description: RequestBody,
		@Part file: MultipartBody.Part
	): AddStoryResponse


	/**
	 * Send get data request from API.
	 *
	 * @param token
	 * @return
	 */
	@GET("stories")
	suspend fun getStories(
		@Header("Authorization") token: String
	): ListStoryResponse
}