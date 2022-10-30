package com.naufaldystd.storyapps.ui.story.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.storyapps.data.FakeStoryInteractor
import com.naufaldystd.storyapps.util.DataDummy
import com.naufaldystd.storyapps.util.MainDispatcherRule
import com.naufaldystd.storyapps.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
class HomeViewModelTest {
	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@OptIn(ExperimentalCoroutinesApi::class)
	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var mockPref: UserPreference
	private lateinit var fakeUseCase: FakeStoryInteractor
	private lateinit var viewModel: HomeViewModel

	private val dummyUser = DataDummy.generateDummyUserModel()
	private val dummyStories = DataDummy.generateDummyStories()

	@Before
	fun setUp() {
		fakeUseCase = FakeStoryInteractor()
		viewModel = HomeViewModel(fakeUseCase, mockPref)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun `when getUser should call mockPref getUser and response not null`() = runTest {
		// Arrange
		val prefResponse = flowOf(dummyUser)

		// Act
		Mockito.`when`(mockPref.getUser()).thenReturn(prefResponse)
		val actualResult = viewModel.getUser().getOrAwaitValue()

		// Assert
		Mockito.verify(mockPref).getUser()
		assertNotNull(actualResult)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun `when getAllStories should return StoryResponse data and not null`() = runTest {
		// Arrange
		val storyResponse = dummyStories
//		val data = PagedTestDataSource.snapshot(storyResponse)
	}

}