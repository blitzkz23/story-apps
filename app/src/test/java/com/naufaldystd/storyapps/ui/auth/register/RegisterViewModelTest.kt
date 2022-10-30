package com.naufaldystd.storyapps.ui.auth.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.naufaldystd.core.data.source.Resource
import com.naufaldystd.storyapps.data.FakeStoryInteractor
import com.naufaldystd.storyapps.util.DataDummy.generateDummyRegisterRequest
import com.naufaldystd.storyapps.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@SmallTest
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	private lateinit var viewModel: RegisterViewModel
	private lateinit var fakeUseCase: FakeStoryInteractor
	private val dummyResponse = "Registered success"

	private val registerRequest = generateDummyRegisterRequest()
	private val dummyEmail = registerRequest.email
	private val dummyPassword = registerRequest.password
	private val dummyName = registerRequest.name

	@Before
	fun setUp() {
		fakeUseCase = FakeStoryInteractor()
		viewModel = RegisterViewModel(fakeUseCase)
	}

	@Test
	fun `when registerAccount should not be null and return resource success`() = runTest {
		// Arrange
		val expectedResult = flow {
			emit(Resource.Success(dummyResponse))
		}

		// Act
		dummyName?.let {
			viewModel.registerAccount(it, dummyEmail, dummyPassword).collect { response ->

				// Assert
				assertNotNull(response)
				assertTrue(response is Resource.Success)
				assertSame(expectedResult, response)
			}
		}
	}
}