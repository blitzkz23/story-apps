package com.naufaldystd.core.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.naufaldystd.core.data.source.local.entity.StoryEntity
import com.naufaldystd.core.data.source.remote.response.StoryResponse

@Dao
interface StoryDao {

	/**
	 * Get all stories from darabase.
	 *
	 * @return
	 */
	@Query("SELECT * FROM story ORDER BY createdAt DESC")
	fun getAllStories(): PagingSource<Int, StoryResponse>

	/**
	 * Insert stories to database for caching purposes.
	 *
	 * @param stories
	 */
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertStories(stories: List<StoryEntity>)

	/**
	 * Delete all data
	 *
	 */
	@Query("DELETE FROM story")
	suspend fun deleteAll()
}