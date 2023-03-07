package com.naufaldystd.storyapps.ui.story.location

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.storyapps.data.FakeStoryInteractor
import com.naufaldystd.storyapps.util.DataDummy
import com.naufaldystd.storyapps.util.DataDummy.generateDummyToken
import com.naufaldystd.storyapps.util.DataDummy.generateDummyUserModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
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
class LocationViewModelTest {
	@get: Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@Mock
	private lateinit var mockPref: UserPreference
	private lateinit var fakeUseCase: FakeStoryInteractor
	private lateinit var locationViewModel: LocationViewModel

	private val dummyToken = generateDummyToken()
	private val dummyUser = generateDummyUserModel()

	@Before
	fun setUp() {
		fakeUseCase = FakeStoryInteractor()
		locationViewModel = LocationViewModel(mockPref, fakeUseCase)
	}

	@Test
	fun `when getUser should call mockPref's getUser and not null`() = runTest {
		// Arrange
		val prefResponse = flowOf(dummyUser)

		// Act
		Mockito.`when`(mockPref.getUser()).thenReturn(prefResponse)
		val actualResult = locationViewModel.getUser()

		// Assert
		Mockito.verify(mockPref).getUser()
		assertNotNull(actualResult)
		assertSame(prefResponse, actualResult)
	}

	@Test
	fun `when getStoriesWithLocation should return Story and not null`() = runTest {
		// Arrange
		val expectedResult = DataDummy.generateDummyStoriesWithLocation()

		// Act
		locationViewModel.getStoriesWithLocation(dummyToken).collect { stories ->
			// Assert
			assertNotNull(stories)
			assertSame(expectedResult.size, stories.data?.size)
		}
	}
}