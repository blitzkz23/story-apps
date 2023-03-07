package com.naufaldystd.core.data.source.local

import com.naufaldystd.core.data.source.local.room.StoryDao
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local data source, connect local data from room database and repository.
 *
 * @property storyDao
 * @constructor Create empty Local data source
 */
@Singleton
class LocalDataSource @Inject constructor(private val storyDao: StoryDao) {

//	fun getAllStories(): Flow<List<StoryEntity>> = storyDao.getAllStories()
//	suspend fun insertStories(stories: List<StoryEntity>) = storyDao.insertStories(stories)
}