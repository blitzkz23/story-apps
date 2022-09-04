package com.naufaldystd.core.data.source

import com.naufaldystd.core.data.source.remote.network.StoryApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

	private var result: Flow<Resource<ResultType>> = flow {
		emit(Resource.Loading())
		val dbSource = loadFromDB()?.first()
		if (shouldFetch(dbSource)) {
			emit(Resource.Loading())
			when (val apiResponse = createCall().first()) {
				is StoryApiResponse.Success -> {
					saveCallResult(apiResponse.data)
					loadFromDB()?.let { data -> emitAll(data.map { Resource.Success(it) }) }
				}
				is StoryApiResponse.Empty -> {
					loadFromDB()?.let { data -> emitAll(data.map { Resource.Success(it) })}
				}
				is StoryApiResponse.Error -> {
					onFetchFailed()
					emit(Resource.Error(apiResponse.errorMessage))
				}
			}
		} else {
			loadFromDB()?.let { data -> emitAll(data.map { Resource.Success(it) }) }
		}
	}

	protected open fun onFetchFailed() {}

	protected abstract fun loadFromDB(): Flow<ResultType>?

	protected abstract fun shouldFetch(data: ResultType?): Boolean

	protected abstract suspend fun createCall(): Flow<StoryApiResponse<RequestType>>

	protected abstract suspend fun saveCallResult(data: RequestType)

	fun asFlow(): Flow<Resource<ResultType>> = result

}