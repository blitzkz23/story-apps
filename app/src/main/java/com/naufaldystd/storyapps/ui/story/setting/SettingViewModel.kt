package com.naufaldystd.storyapps.ui.story.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val pref: UserPreference) : ViewModel() {
	fun getUser(): Flow<UserModel> {
		return pref.getUser()
	}

	fun logOutUser() {
		viewModelScope.launch {
			pref.logout()
		}
	}
}