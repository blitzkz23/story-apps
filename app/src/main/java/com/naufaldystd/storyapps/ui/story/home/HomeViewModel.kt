package com.naufaldystd.storyapps.ui.story.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
	private val storyUseCase: StoryUseCase,
	private val pref: UserPreference
) : ViewModel() {
	fun getUser(): LiveData<UserModel> {
		return pref.getUser().asLiveData()
	}

	fun getAllStories(token: String) = storyUseCase.getAllStories(token).asLiveData()
}