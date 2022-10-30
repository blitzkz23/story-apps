package com.naufaldystd.storyapps.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.storyapps.util.MainDispatcherRule
import com.naufaldystd.storyapps.data.FakeStoryInteractor
import com.naufaldystd.storyapps.util.DataDummy.generateDummyLoginRequest
import com.naufaldystd.storyapps.util.DataDummy.generateDummyUserModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@SmallTest
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var mockPref: UserPreference
	private lateinit var fakeUseCase: FakeStoryInteractor
	private lateinit var loginViewModel: LoginViewModel

	private val dummyUser = generateDummyUserModel()

	private val loginRequest = generateDummyLoginRequest()
	private val dummyEmail = loginRequest.email
	private val dummyPassword = loginRequest.password

	@Before
	fun setUp() {
		fakeUseCase = FakeStoryInteractor()
		loginViewModel = LoginViewModel(fakeUseCase, mockPref)
	}

	@Test
	fun `when loginAccount should not be null and return resource success`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(Resource.Success(dummyUser))
		}

		// Act
		val actualResult = loginViewModel.loginAccount(dummyEmail, dummyPassword).collect { user ->

			// Assert
			assertNotNull(user)
			assertTrue(user is Resource.Success)
			assertSame(expectedResult, user)
		}
	}

	@Test
	fun `when logUser should log the data of logged in user and call mockPref saveUserSession`() = runTest {
		// Arrange
		val expectedUserData = generateDummyUserModel()

		// Act
		val actualUserData = loginViewModel.logUser(expectedUserData)

		// Assert
		Mockito.verify(mockPref).saveUserSession(expectedUserData)
		assertNotNull(actualUserData)
	}

	@Test
	fun `when Network error should return error`() = runTest {
		// Arrange expected result
		val expectedResult = flow {
			emit(Resource.Error<String>("Error"))
		}

		// Act
		val actualResult = loginViewModel.loginAccount(dummyEmail, dummyPassword).collect { user ->

			// Assert
			assertNotNull(user)
			assertTrue(user is Resource.Error)
			assertSame(expectedResult, user)
		}
	}
}