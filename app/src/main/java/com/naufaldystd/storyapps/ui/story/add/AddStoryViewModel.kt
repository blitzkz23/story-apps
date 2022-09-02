package com.naufaldystd.storyapps.ui.story.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(private val storyUseCase: StoryUseCase, private val pref: UserPreference) : ViewModel() {
	suspend fun addStory(token: String, description: RequestBody, photo: MultipartBody.Part) =
		storyUseCase.addStory(token, description, photo).asLiveData()

	fun getUser(): LiveData<UserModel> {
		return pref.getUser().asLiveData()
	}
}