package com.naufaldystd.storyapps.ui.story.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.naufaldystd.core.data.source.remote.response.StoryResponse
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
	suspend fun loginAccount(email: String, password: String) =
		storyUseCase.loginAccount(email, password)

	fun getUser(): LiveData<UserModel> {
		return pref.getUser().asLiveData()
	}

	fun getAllStories(token: String): LiveData<PagingData<StoryResponse>> =
		storyUseCase.getAllStories(token).cachedIn(viewModelScope).asLiveData()
}