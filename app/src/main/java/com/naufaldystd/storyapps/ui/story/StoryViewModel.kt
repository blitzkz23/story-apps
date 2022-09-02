package com.naufaldystd.storyapps.ui.story

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class StoryViewModel @Inject constructor(private val storyUseCase: StoryUseCase, private val pref: UserPreference): ViewModel() {

	fun getUser(): LiveData<UserModel> {
		return pref.getUser().asLiveData()
	}

	fun getAllStories(token: String) = storyUseCase.getAllStories(token).asLiveData()
}