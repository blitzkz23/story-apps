package com.naufaldystd.storyapps.ui.story.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val pref: UserPreference) : ViewModel() {
	fun getUser(): LiveData<UserModel> {
		return pref.getUser().asLiveData()
	}

	fun logOutUser() {
		viewModelScope.launch {
			pref.logout()
		}
	}
}