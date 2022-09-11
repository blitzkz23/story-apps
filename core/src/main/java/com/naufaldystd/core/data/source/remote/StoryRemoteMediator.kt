package com.naufaldystd.core.data.source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.naufaldystd.core.data.source.local.room.RemoteKeys
import com.naufaldystd.core.data.source.local.room.StoryDatabase
import com.naufaldystd.core.data.source.remote.network.ApiService
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.utils.DataMapper

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
	private val database: StoryDatabase,
	private val apiService: ApiService,
	private val token: String,
	private val location: Int? = null
) : RemoteMediator<Int, StoryResponse>() {

	override suspend fun load(
		loadType: LoadType,
		state: PagingState<Int, StoryResponse>
	): MediatorResult {
		val page = when (loadType) {
			LoadType.REFRESH -> {
				val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
				remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
			}
			LoadType.PREPEND -> {
				val remoteKeys = getRemoteKeyForFirstItem(state)
				val prevKey = remoteKeys?.prevKey
					?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
				prevKey
			}
			LoadType.APPEND -> {
				val remoteKeys = getRemoteKeyForLastItem(state)
				val nextKey = remoteKeys?.nextKey
					?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
				nextKey
			}
		}

		return try {
			val responses =
				apiService.getAllStories(token, page, state.config.pageSize, location = location)
			val responseData = responses.listStory
			val endOfPaginationReached = responseData.isEmpty()

			database.withTransaction {
				if (loadType == LoadType.REFRESH) {
					database.remoteKeysDao().deleteRemoteKeys()
					database.storyDao().deleteAll()
				}
				val prevKey = if (page == 1) null else page - 1
				val nextKey = if (endOfPaginationReached) null else page + 1
				val keys = responseData.map {
					RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
				}
				database.remoteKeysDao().insertAll(keys)

				// Convert responses into entity for database
				val storyEntities = DataMapper.mapStoryResponsesToEntities(responseData)
				database.storyDao().insertStories(storyEntities)
			}
			MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
		} catch (e: Exception) {
			MediatorResult.Error(e)
		}
	}

	/**
	 * Get remote key for last item
	 *
	 * @param state
	 * @return
	 */
	private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryResponse>): RemoteKeys? {
		return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
			database.remoteKeysDao().getRemoteKeysId(data.id)
		}
	}

	/**
	 * Get remote key for first item
	 *
	 * @param state
	 * @return
	 */
	private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryResponse>): RemoteKeys? {
		return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
			database.remoteKeysDao().getRemoteKeysId(data.id)
		}
	}

	/**
	 * Get remote key closest to current position
	 *
	 * @param state
	 * @return
	 */
	private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryResponse>): RemoteKeys? {
		return state.anchorPosition?.let { position ->
			state.closestItemToPosition(position)?.id?.let { id ->
				database.remoteKeysDao().getRemoteKeysId(id)
			}
		}
	}

	private companion object {
		const val INITIAL_PAGE_INDEX = 1
	}
}