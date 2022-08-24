package com.naufaldystd.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.naufaldystd.core.data.source.local.entity.StoryEntity

@Database(entities = [StoryEntity::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {

	abstract fun storyDao(): StoryDao
}