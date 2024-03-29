package com.naufaldystd.core.di

import android.content.Context
import androidx.room.Room
import com.naufaldystd.core.data.source.local.room.RemoteKeysDao
import com.naufaldystd.core.data.source.local.room.StoryDao
import com.naufaldystd.core.data.source.local.room.StoryDatabase
import com.naufaldystd.core.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

	@Singleton
	@Provides
	fun provideDatabase(@ApplicationContext context: Context): StoryDatabase {

		return Room.databaseBuilder(
			context,
			StoryDatabase::class.java, DB_NAME
		)
			.fallbackToDestructiveMigration()
			.build()
	}

	@Provides
	fun provideStoryDao(database: StoryDatabase): StoryDao = database.storyDao()

	@Provides
	fun provideRemoteKeysDao(database: StoryDatabase): RemoteKeysDao = database.remoteKeysDao()
}