package com.naufaldystd.core.data.source

import com.naufaldystd.core.data.source.remote.RemoteDataSource
import com.naufaldystd.core.domain.repository.StoryRepository
import com.naufaldystd.core.utils.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(
	private val remoteDataSource: RemoteDataSource,
	private val appExecutors: AppExecutors
) : StoryRepository {

}