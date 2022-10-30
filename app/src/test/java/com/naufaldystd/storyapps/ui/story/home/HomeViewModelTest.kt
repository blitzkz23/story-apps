package com.naufaldystd.storyapps.ui.story.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.test.filters.SmallTest
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.storyapps.data.FakeStoryInteractor
import com.naufaldystd.storyapps.ui.story.home.adapter.StoryAdapter
import com.naufaldystd.storyapps.util.DataDummy.generateDummyStories
import com.naufaldystd.storyapps.util.DataDummy.generateDummyToken
import com.naufaldystd.storyapps.util.DataDummy.generateDummyUserModel
import com.naufaldystd.storyapps.util.MainDispatcherRule
import com.naufaldystd.storyapps.util.StoryPagingSource
import com.naufaldystd.storyapps.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@SmallTest
@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var mockPref: UserPreference
	private lateinit var fakeUseCase: FakeStoryInteractor
	private lateinit var homeViewModel: HomeViewModel

	@Mock
	private lateinit var homeViewModelMock: HomeViewModel

	private val dummyUser = generateDummyUserModel()
	private val dummyStories = generateDummyStories()
	private val dummyToken = generateDummyToken()

	@Before
	fun setUp() {
		fakeUseCase = FakeStoryInteractor()
		homeViewModel = HomeViewModel(fakeUseCase, mockPref)
	}


	@Test
	fun `when getUser should call mockPref's getUser and response not null`() = runTest {
		// Arrange
		val prefResponse = flowOf(dummyUser)

		// Act
		Mockito.`when`(mockPref.getUser()).thenReturn(prefResponse)
		val actualResult = homeViewModel.getUser().getOrAwaitValue()

		// Assert
		Mockito.verify(mockPref).getUser()
		assertNotNull(actualResult)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun `when getAllStories should return StoryResponse data and not null`() = runTest {
		// Arrange
		val storyResponse = dummyStories
		val data = StoryPagingSource.snapshot(storyResponse)

		val stories = MutableLiveData<PagingData<StoryResponse>>()
		stories.value = data

		Mockito.`when`(homeViewModelMock.getAllStories(dummyToken)).thenReturn(stories)
		val actualStories = homeViewModelMock.getAllStories(dummyToken).getOrAwaitValue()

		val differ = AsyncPagingDataDiffer(
			diffCallback = StoryAdapter.DIFF_CALLBACK,
			updateCallback = noopListUpdateCallback,
			mainDispatcher = Dispatchers.Main
		)
		differ.submitData(actualStories)
		advanceUntilIdle()

		Mockito.verify(homeViewModelMock).getAllStories(dummyToken)
		assertNotNull(differ.snapshot())
		assertEquals(storyResponse.size, differ.snapshot().size)
	}

	private val noopListUpdateCallback = object : ListUpdateCallback {
		override fun onInserted(position: Int, count: Int) {}
		override fun onRemoved(position: Int, count: Int) {}
		override fun onMoved(fromPosition: Int, toPosition: Int) {}
		override fun onChanged(position: Int, count: Int, payload: Any?) {}
	}

}