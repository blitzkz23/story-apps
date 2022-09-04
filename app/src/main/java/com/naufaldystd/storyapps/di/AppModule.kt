package com.naufaldystd.storyapps.di

import com.naufaldystd.core.domain.usecase.StoryInteractor
import com.naufaldystd.core.domain.usecase.StoryUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

	@Binds
	@Singleton
	abstract fun provideUseCase(storyInteractor: StoryInteractor): StoryUseCase

}