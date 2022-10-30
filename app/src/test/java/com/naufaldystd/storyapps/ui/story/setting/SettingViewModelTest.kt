package com.naufaldystd.storyapps.ui.story.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.naufaldystd.core.domain.model.UserPreference
import com.naufaldystd.storyapps.util.DataDummy.generateDummyUserModel
import com.naufaldystd.storyapps.util.MainDispatcherRule
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
class SettingViewModelTest {
	@get:Rule
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()

	@Mock
	private lateinit var mockPref: UserPreference
	private lateinit var settingViewModel: SettingViewModel

	private val dummyUser = generateDummyUserModel()

	@Before
	fun setUp() {
		settingViewModel = SettingViewModel(mockPref)
	}

	@Test
	fun `when getUser should call mockPref's getUser and not null`() = runTest {
		// Arrange
		val prefResponse = flowOf(dummyUser)

		// Act
		Mockito.`when`(mockPref.getUser()).thenReturn(prefResponse)
		val actualResult = settingViewModel.getUser()

		// Assert
		Mockito.verify(mockPref).getUser()
		assertNotNull(actualResult)
		assertSame(prefResponse, actualResult)
	}

	@Test
	fun `when logOutUser should call mockPref logout`() = runTest {
		settingViewModel.logOutUser()
		Mockito.verify(mockPref).logout()
	}
}