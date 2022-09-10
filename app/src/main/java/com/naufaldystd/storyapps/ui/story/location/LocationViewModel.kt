package com.naufaldystd.storyapps.ui.story.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val storyUseCase: StoryUseCase) : ViewModel() {
	fun getStoriesWithLocation(token: String) =
		storyUseCase.getAllStories(token, location = 1).asLiveData()
}