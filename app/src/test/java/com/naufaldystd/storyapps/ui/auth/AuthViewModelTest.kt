package com.naufaldystd.storyapps.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.storyapps.util.DataDummy
import com.naufaldystd.storyapps.util.MainDispatcherRule

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
class AuthViewModelTest {
	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@OptIn(ExperimentalCoroutinesApi::class)
	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var mockPref: UserPreference
	private lateinit var authViewModel: AuthViewModel

	private val dummyUser = DataDummy.generateDummyUserModel()

	@Before
	fun setUp() {
		authViewModel = AuthViewModel(mockPref)
	}

	@Test
	fun `when getUser should call mockPref getUser and not null`() = runTest {
		// Arrange
		val prefResponse = flowOf(dummyUser)

		// Act
		Mockito.`when`(mockPref.getUser()).thenReturn(prefResponse)
		val actualResult = authViewModel.getUser()

		// Assert
		Mockito.verify(mockPref).getUser()
		assertNotNull(actualResult)
	}
}