package com.naufaldystd.core.data.source

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.naufaldystd.core.data.source.local.room.StoryDatabase
import com.naufaldystd.core.data.source.remote.FakeApiService
import com.naufaldystd.core.data.source.remote.StoryRemoteMediator
import com.naufaldystd.core.data.source.remote.network.ApiService
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

	private var dummyToken = "dummyToken"
	private var fakeApi: ApiService = FakeApiService()
	private var fakeDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
		ApplicationProvider.getApplicationContext(),
		StoryDatabase::class.java
	).allowMainThreadQueries().build()

	@After
	fun tearDown() {
		fakeDb.clearAllTables()
	}

	@Test
	fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
		// Arrange
		val pagingState = PagingState<Int, StoryResponse>(
			listOf(),
			null,
			PagingConfig(10),
			10
		)
		val remoteMediator = StoryRemoteMediator(
			fakeDb,
			fakeApi,
			dummyToken
		)

		// Act
		val result = remoteMediator.load(LoadType.REFRESH, pagingState)

		// Assert
		assertTrue(result is RemoteMediator.MediatorResult.Success)
		assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
	}
}