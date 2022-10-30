package com.naufaldystd.core.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.naufaldystd.core.data.source.local.LocalDataSource
import com.naufaldystd.core.data.source.local.room.StoryDatabase
import com.naufaldystd.core.data.source.remote.RemoteDataSource
import com.naufaldystd.core.data.source.remote.network.ApiService
import com.naufaldystd.core.data.source.remote.network.StoryApiResponse
import com.naufaldystd.storyapps.util.DataDummy.generateDummyErrorMessage
import com.naufaldystd.storyapps.util.DataDummy.generateDummyLoginRequest
import com.naufaldystd.storyapps.util.DataDummy.generateDummyLoginResult
import com.naufaldystd.storyapps.util.DataDummy.generateDummyRegisterRequest
import com.naufaldystd.storyapps.util.DataDummy.generateDummyStoriesWithLocation
import com.naufaldystd.storyapps.util.DataDummy.generateDummyStoryRequest
import com.naufaldystd.storyapps.util.DataDummy.generateDummySuccessMessage
import com.naufaldystd.storyapps.util.DataDummy.generateDummyToken
import com.naufaldystd.storyapps.util.DataDummy.generateDummyUserCreatedMessage
import com.naufaldystd.storyapps.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
class StoryRepositoryImplTest {
	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var remoteDataSource: RemoteDataSource

	@Mock
	private lateinit var localDataSource: LocalDataSource

	@Mock
	private lateinit var storyDatabase: StoryDatabase

	@Mock
	private lateinit var apiService: ApiService
	private lateinit var storyRepositoryImpl: StoryRepositoryImpl

	private var dummyLoginResult = generateDummyLoginResult()
	private var dummyLoginRequest = generateDummyLoginRequest()
	private var dummyRegisterRequest = generateDummyRegisterRequest()
	private var dummyStoryRequest = generateDummyStoryRequest()

	private var dummyStoriesWithLocation = generateDummyStoriesWithLocation()

	private var dummyToken = generateDummyToken()
	private var dummyUserCreated = generateDummyUserCreatedMessage()
	private var dummySuccessMsg = generateDummySuccessMessage()
	private var dummyError = generateDummyErrorMessage()

	@Before
	fun setUp() {
		storyRepositoryImpl =
			StoryRepositoryImpl(remoteDataSource, localDataSource, storyDatabase, apiService)
	}


	@Test
	fun `when registerAccount should call remote data source's registerAccount and not null`() =
		runTest {
			// Arrange
			val expectedResult = flow {
				emit(StoryApiResponse.Success(dummyUserCreated))
			}

			// Act
			Mockito.`when`(
				remoteDataSource.registerAccount(
					dummyRegisterRequest.name!!,
					dummyRegisterRequest.email, dummyRegisterRequest.password
				)
			).thenReturn(expectedResult)

			storyRepositoryImpl.registerAccount(
				dummyRegisterRequest.name!!,
				dummyRegisterRequest.email,
				dummyRegisterRequest.password
			).collect { response ->
				assertNotNull(response)
				assertTrue(response is Resource.Success)
			}

			Mockito.verify(remoteDataSource).registerAccount(
				dummyRegisterRequest.name!!,
				dummyRegisterRequest.email,
				dummyRegisterRequest.password
			)
		}

	@Test
	fun `when registerAccount error occured should throw exception`() =
		runTest {
			// Arrange
			val expectedResult = flow {
				emit(StoryApiResponse.Error(dummyError))
			}

			// Act
			Mockito.`when`(
				remoteDataSource.registerAccount(
					dummyRegisterRequest.name!!,
					dummyRegisterRequest.email, dummyRegisterRequest.password
				)
			).thenReturn(expectedResult)

			storyRepositoryImpl.registerAccount(
				dummyRegisterRequest.name!!,
				dummyRegisterRequest.email,
				dummyRegisterRequest.password
			).collect { response ->
				assertNotNull(response)
				assertTrue(response is Resource.Error)
			}

			Mockito.verify(remoteDataSource).registerAccount(
				dummyRegisterRequest.name!!,
				dummyRegisterRequest.email,
				dummyRegisterRequest.password
			)
		}

	@Test
	fun `when loginAccount should call remote data source's loginAccount and not null`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(StoryApiResponse.Success(dummyLoginResult))
		}

		// Act
		Mockito.`when`(
			remoteDataSource.loginAccount(
				dummyLoginRequest.email,
				dummyLoginRequest.password
			)
		).thenReturn(expectedResult)

		storyRepositoryImpl.loginAccount(
			dummyLoginRequest.email,
			dummyLoginRequest.password
		).collect { response ->

			// Assert
			assertNotNull(response)
			assertTrue(response is Resource.Success)
		}

		Mockito.verify(remoteDataSource).loginAccount(
			dummyLoginRequest.email,
			dummyLoginRequest.password
		)
	}

	@Test
	fun `when loginAccount error occured should throw exception`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(StoryApiResponse.Error(dummyError))
		}

		// Act
		Mockito.`when`(
			remoteDataSource.loginAccount(
				dummyLoginRequest.email,
				dummyLoginRequest.password
			)
		).thenReturn(expectedResult)

		storyRepositoryImpl.loginAccount(
			dummyLoginRequest.email,
			dummyLoginRequest.password
		).collect { response ->

			// Assert
			assertNotNull(response)
			assertTrue(response is Resource.Error)
		}

		Mockito.verify(remoteDataSource).loginAccount(
			dummyLoginRequest.email,
			dummyLoginRequest.password
		)
	}

	@Test
	fun `when addStory should call remote data source's addStory and not null`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(StoryApiResponse.Success(dummySuccessMsg))
		}

		// Act
		Mockito.`when`(
			remoteDataSource.addStory(
				dummyStoryRequest.token,
				dummyStoryRequest.description, dummyStoryRequest.photo,
				dummyStoryRequest.lat,
				dummyStoryRequest.lon
			)
		).thenReturn(expectedResult)

		storyRepositoryImpl.addStory(
			dummyStoryRequest.token,
			dummyStoryRequest.description, dummyStoryRequest.photo,
			dummyStoryRequest.lat,
			dummyStoryRequest.lon
		).collect { response ->
			assertNotNull(response)
			assertTrue(response is Resource.Success)
		}

		Mockito.verify(remoteDataSource).addStory(
			dummyStoryRequest.token,
			dummyStoryRequest.description, dummyStoryRequest.photo,
			dummyStoryRequest.lat,
			dummyStoryRequest.lon
		)
	}

	@Test
	fun `when addStory error occured should throw exception`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(StoryApiResponse.Error(dummyError))
		}

		// Act
		Mockito.`when`(
			remoteDataSource.addStory(
				dummyStoryRequest.token,
				dummyStoryRequest.description, dummyStoryRequest.photo,
				dummyStoryRequest.lat,
				dummyStoryRequest.lon
			)
		).thenReturn(expectedResult)

		storyRepositoryImpl.addStory(
			dummyStoryRequest.token,
			dummyStoryRequest.description, dummyStoryRequest.photo,
			dummyStoryRequest.lat,
			dummyStoryRequest.lon
		).collect { response ->
			assertNotNull(response)
			assertTrue(response is Resource.Error)
		}

		Mockito.verify(remoteDataSource).addStory(
			dummyStoryRequest.token,
			dummyStoryRequest.description, dummyStoryRequest.photo,
			dummyStoryRequest.lat,
			dummyStoryRequest.lon
		)
	}

	@Test
	fun `when addStoryGuest should call remote data source's addStoryGuest and not null`() =
		runTest {
			// Arrange
			val expectedResult = flow {
				emit(StoryApiResponse.Success(dummySuccessMsg))
			}

			// Act
			Mockito.`when`(
				remoteDataSource.addStoryGuest(
					dummyStoryRequest.description, dummyStoryRequest.photo,
					dummyStoryRequest.lat,
					dummyStoryRequest.lon
				)
			).thenReturn(expectedResult)

			storyRepositoryImpl.addStoryGuest(
				dummyStoryRequest.description, dummyStoryRequest.photo,
				dummyStoryRequest.lat,
				dummyStoryRequest.lon
			).collect { response ->
				assertNotNull(response)
				assertTrue(response is Resource.Success)
			}

			Mockito.verify(remoteDataSource).addStoryGuest(
				dummyStoryRequest.description, dummyStoryRequest.photo,
				dummyStoryRequest.lat,
				dummyStoryRequest.lon
			)
		}

	@Test
	fun `when addStoryGuest error occured should throw exception`() =
		runTest {
			// Arrange
			val expectedResult = flow {
				emit(StoryApiResponse.Error(dummyError))
			}

			// Act
			Mockito.`when`(
				remoteDataSource.addStoryGuest(
					dummyStoryRequest.description, dummyStoryRequest.photo,
					dummyStoryRequest.lat,
					dummyStoryRequest.lon
				)
			).thenReturn(expectedResult)

			storyRepositoryImpl.addStoryGuest(
				dummyStoryRequest.description, dummyStoryRequest.photo,
				dummyStoryRequest.lat,
				dummyStoryRequest.lon
			).collect { response ->
				assertNotNull(response)
				assertTrue(response is Resource.Error)
			}

			Mockito.verify(remoteDataSource).addStoryGuest(
				dummyStoryRequest.description, dummyStoryRequest.photo,
				dummyStoryRequest.lat,
				dummyStoryRequest.lon
			)
		}

	@Test
	fun `when getStoriesWithLocation should call remote data sources's getAllStories and not null`() =
		runTest {
			// Arrange
			val expectedResult = channelFlow {
				send(StoryApiResponse.Success(dummyStoriesWithLocation))
			}

			// Act
			Mockito.`when`(remoteDataSource.getAllStories(dummyToken, location = 1))
				.thenReturn(expectedResult)

			storyRepositoryImpl.getStoriesWithLocation(dummyToken).collect { response ->
				assertNotNull(response)
				assertTrue(response is Resource.Success)
			}

			Mockito.verify(remoteDataSource).getAllStories(dummyToken, location = 1)
		}

	@Test
	fun `when getStoriesWithLocation error occured should return error message and not null`() =
		runTest {
			// Arrange
			val expectedResult = channelFlow {
				send(StoryApiResponse.Error(dummyError))
			}

			// Act
			Mockito.`when`(remoteDataSource.getAllStories(dummyToken, location = 1)).thenReturn(expectedResult)

			storyRepositoryImpl.getStoriesWithLocation(dummyToken).collect { response ->
				// Assert
				assertNotNull(response)
				assertTrue(response is Resource.Error)
			}

			Mockito.verify(remoteDataSource).getAllStories(dummyToken, location = 1)
		}

}