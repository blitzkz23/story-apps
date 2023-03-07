package com.naufaldystd.storyapps.ui.story.home

import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.utils.EspressoIdlingResource
import com.naufaldystd.storyapps.utils.launchFragmentInHiltContainer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

	@Before
	fun setUp() {
		IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
	}

	@After
	fun tearDown() {
		IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
	}

	@Test
	fun loadStories_Success() {
		launchFragmentInHiltContainer<HomeFragment>()
		onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
		onView(withId(R.id.rv_story)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
	}

	@Test
	fun loadDetailStory_Success() {
		launchFragmentInHiltContainer<HomeFragment>()
		onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
		onView(withId(R.id.rv_story)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
		pressBack()
	}
}