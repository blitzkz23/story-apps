package com.naufaldystd.storyapps.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufaldystd.core.domain.model.UserModel
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val storyUseCase: StoryUseCase, private val pref: UserPreference) : ViewModel() {
	suspend fun loginAccount(email: String, password: String) =
		storyUseCase.loginAccount(email, password)

	fun logUser(user: UserModel) {
		viewModelScope.launch {
			pref.saveUserSession(user)
		}
	}
}