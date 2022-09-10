package com.naufaldystd.storyapps.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val pref: UserPreference) : ViewModel() {
	fun getUser(): LiveData<UserModel> {
		return pref.getUser().asLiveData()
	}
}