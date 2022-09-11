package com.naufaldystd.storyapps.ui.story.add

import androidx.lifecycle.ViewModel
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
	private val storyUseCase: StoryUseCase,
	private val pref: UserPreference
) : ViewModel() {
	suspend fun addStory(
		token: String,
		description: RequestBody,
		photo: MultipartBody.Part,
		lat: RequestBody? = null,
		lon: RequestBody? = null
	) =
		storyUseCase.addStory(token, description, photo, lat = lat, lon = lon)

	suspend fun addStoryGuest(
		description: RequestBody,
		photo: MultipartBody.Part,
		lat: RequestBody? = null,
		lon: RequestBody? = null
	) =
		storyUseCase.addStoryGuest(description, photo, lat = lat, lon = lon)

	fun getUser(): Flow<UserModel> {
		return pref.getUser()
	}
}