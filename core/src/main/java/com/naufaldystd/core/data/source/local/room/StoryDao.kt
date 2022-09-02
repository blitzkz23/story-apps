package com.naufaldystd.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.naufaldystd.core.data.source.local.entity.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {

	@Query("SELECT * FROM story ORDER BY createdAt DESC")
	fun getAllStories(): Flow<List<StoryEntity>>

	@Query("SELECT * FROM story WHERE id =:id ")
	fun getStoryById(id: String): Flow<StoryEntity>?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertStories(stories: List<StoryEntity>)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertStory(story: StoryEntity)
}