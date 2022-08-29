package com.naufaldystd.storyapps.ui.login

import androidx.lifecycle.ViewModel
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val storyUseCase: StoryUseCase) : ViewModel() {
	suspend fun loginAccount(email: String, password: String) =
		storyUseCase.loginAccount(email, password)
}