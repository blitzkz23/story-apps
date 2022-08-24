package com.naufaldystd.core.data.source.local

import com.naufaldystd.core.data.source.local.entity.StoryEntity
import com.naufaldystd.core.data.source.local.room.StoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val storyDao: StoryDao){

	fun getAllStories(): Flow<List<StoryEntity>> = storyDao.getAllStories()
	fun getStoryById(id: String): Flow<StoryEntity>? = storyDao.getStoryById(id)
	suspend fun insertStories(stories: List<StoryEntity>) = storyDao.insertStories(stories)
	suspend fun insertStory(story: StoryEntity) = storyDao.insertStory(story)
}