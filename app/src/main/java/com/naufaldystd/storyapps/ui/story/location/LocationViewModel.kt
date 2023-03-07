package com.naufaldystd.storyapps.ui.story.location

import androidx.lifecycle.ViewModel
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
	private val pref: UserPreference,
	private val storyUseCase: StoryUseCase
) : ViewModel() {
	fun getUser(): Flow<UserModel> {
		return pref.getUser()
	}

	fun getStoriesWithLocation(token: String) =
		storyUseCase.getStoriesWithLocation(token)
}