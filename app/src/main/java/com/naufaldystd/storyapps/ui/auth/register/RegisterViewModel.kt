package com.naufaldystd.storyapps.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val storyUseCase: StoryUseCase) : ViewModel() {
	suspend fun registerAccount(name: String, email: String, password: String) =
		storyUseCase.registerAccount(name, email, password)
}