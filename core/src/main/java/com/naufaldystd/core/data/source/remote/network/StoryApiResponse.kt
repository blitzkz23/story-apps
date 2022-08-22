package com.naufaldystd.core.data.source.remote.network

sealed class StoryApiResponse<out T> {
	data class Success<T>(val data: T): StoryApiResponse<T>()
	data class Error(val errorMessage: String) : StoryApiResponse<Nothing>()
	object Empty : StoryApiResponse<Nothing>()
}
