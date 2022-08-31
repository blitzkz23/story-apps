package com.naufaldystd.storyapps.ui.story

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel()
class StoryViewModel @Inject constructor(private val storyUseCase: StoryUseCase, private val dataStore: DataStore<Preferences>): ViewModel() {
	private var token: String = ""

	fun setToken(token: String) {
		this.token = token
	}

	val story = storyUseCase.getAllStories(token).asLiveData()
}