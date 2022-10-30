package com.naufaldystd.storyapps.ui.story.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.storyapps.data.FakeStoryInteractor
import com.naufaldystd.storyapps.util.DataDummy.generateDummyErrorMessage
import com.naufaldystd.storyapps.util.DataDummy.generateDummyStoryRequest
import com.naufaldystd.storyapps.util.DataDummy.generateDummySuccessMessage
import com.naufaldystd.storyapps.util.DataDummy.generateDummyUserModel
import com.naufaldystd.storyapps.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
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
class AddStoryViewModelTest {
	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var mockPref: UserPreference
	private lateinit var fakeUseCase: FakeStoryInteractor
	private lateinit var addStoryViewModel: AddStoryViewModel

	private val dummyUser = generateDummyUserModel()
	private val dummySuccessMsg = generateDummySuccessMessage()
	private val dummyErrorMsg = generateDummyErrorMessage()
	private val dummyRequest = generateDummyStoryRequest()

	@Before
	fun setUp() {
		fakeUseCase = FakeStoryInteractor()
		addStoryViewModel = AddStoryViewModel(fakeUseCase, mockPref)
	}


	@Test
	fun `when getUser should call mockPref's getUser and not null`() = runTest {
		// Arrange
		val prefResponse = flowOf(dummyUser)

		// Act
		Mockito.`when`(mockPref.getUser()).thenReturn(prefResponse)
		val actualResult = addStoryViewModel.getUser()

		// Assert
		Mockito.verify(mockPref).getUser()
		assertNotNull(actualResult)
		assertSame(prefResponse, actualResult)
	}

	@Test
	fun `when addStory should not be null and return resource success`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(Resource.Success(dummySuccessMsg))
		}

		// Act
		addStoryViewModel.addStory(
			dummyRequest.token,
			dummyRequest.description,
			dummyRequest.photo,
			dummyRequest.lat,
			dummyRequest.lon
		).collect { response ->

			// Assert
			assertNotNull(response)
			assertSame(expectedResult, response)
			assertTrue(response is Resource.Success)
		}
	}

	@Test
	fun `when addStory Network error should return error`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(Resource.Error<String>(dummyErrorMsg))
		}

		// Act
		addStoryViewModel.addStory(
			dummyRequest.token,
			dummyRequest.description,
			dummyRequest.photo,
			dummyRequest.lat,
			dummyRequest.lon
		).collect { response ->

			// Assert
			assertNotNull(response)
			assertTrue(response is Resource.Error)
			assertSame(expectedResult, response)
		}
	}

	@Test
	fun `when addStoryGuest should not be null and return resource success`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(Resource.Success(dummySuccessMsg))
		}

		// Act
		addStoryViewModel.addStoryGuest(
			dummyRequest.description,
			dummyRequest.photo,
			dummyRequest.lat,
			dummyRequest.lon
		).collect { response ->

			// Assert
			assertNotNull(response)
			assertSame(expectedResult, response)
			assertTrue(response is Resource.Success)
		}
	}

	@Test
	fun `when addStoryGuest Network error should return error`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(Resource.Error<String>(dummyErrorMsg))
		}

		// Act
		addStoryViewModel.addStoryGuest(
			dummyRequest.description,
			dummyRequest.photo,
			dummyRequest.lat,
			dummyRequest.lon
		).collect { response ->

			// Assert
			assertNotNull(response)
			assertTrue(response is Resource.Error)
			assertSame(expectedResult, response)
		}
	}
}