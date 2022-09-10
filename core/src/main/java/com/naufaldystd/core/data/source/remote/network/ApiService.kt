package com.naufaldystd.core.data.source.remote.network

import com.naufaldystd.core.data.source.remote.response.AddStoryResponse
import com.naufaldystd.core.data.source.remote.response.ListStoryResponse
import com.naufaldystd.core.data.source.remote.response.LoginResponse
import com.naufaldystd.core.data.source.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

	/**
	 * Send register account request to API.
	 *
	 * @param name User's name
	 * @param email User's email
	 * @param password User's password'
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
	 * @param email User's email
	 * @param password User's password
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
	 * @param token Authentication token
	 * @param description Description of the image
	 * @param file Multipart image
	 * @return
	 */
	@Multipart
	@POST("stories")
	suspend fun addStory(
		@Header("Authorization") token: String,
		@Part("description") description: RequestBody,
		@Part file: MultipartBody.Part,
		@Part("lat") lat: RequestBody? = null,
		@Part("lon") lon: RequestBody? = null
	): AddStoryResponse

	/**
	 * Send create new story request to API for guest user.
	 *
	 * @param description Description of the image
	 * @param file Multipart image
	 * @return
	 */
	@Multipart
	@POST("stories/guest")
	suspend fun addStoryGuest(
		@Part("description") description: RequestBody,
		@Part file: MultipartBody.Part,
		@Part("lat") lat: RequestBody? = null,
		@Part("lon") lon: RequestBody? = null
	): AddStoryResponse


	/**
	 * Send get data request from API.
	 *
	 * @param token Authentication token from API
	 * @param page Page of data from API
	 * @param size Size of data from API
	 * @param location Location of story from API
	 * @return
	 */
	@GET("stories")
	suspend fun getAllStories(
		@Header("Authorization") token: String,
		@Query("page") page: Int? = null,
		@Query("size") size: Int? = null,
		@Query("location") location: Int? = null
	): ListStoryResponse
}