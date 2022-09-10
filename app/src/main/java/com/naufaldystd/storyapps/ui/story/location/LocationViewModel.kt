package com.naufaldystd.storyapps.ui.story.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
	private val pref: UserPreference,
	private val storyUseCase: StoryUseCase
) : ViewModel() {
	fun getUser(): LiveData<UserModel> {
		return pref.getUser().asLiveData()
	}

	fun getStoriesWithLocation(token: String) =
		storyUseCase.getAllStories(token, location = 1).asLiveData()
}