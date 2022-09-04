package com.naufaldystd.core.di

import com.naufaldystd.core.data.source.StoryRepositoryImpl
import com.naufaldystd.core.domain.repository.StoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

	@Binds
	abstract fun providesRepository(storyRepositoryImpl: StoryRepositoryImpl) : StoryRepository
}