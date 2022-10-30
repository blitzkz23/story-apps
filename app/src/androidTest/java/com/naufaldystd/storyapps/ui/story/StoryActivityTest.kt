package com.naufaldystd.storyapps.ui.story

import android.provider.ContactsContract
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.bumptech.glide.Glide.init
import com.naufaldystd.storyapps.utils.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.naufaldystd.storyapps.ui.auth.AuthActivity
import com.naufaldystd.storyapps.R

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class StoryActivityTest {

	@get:Rule
	val activity = ActivityScenarioRule(StoryActivity::class.java)

	@Before
	fun setUp() {
		Intents.init()
		IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
	}

	@After
	fun tearDown() {
		IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
	}

	@Test
	fun openAllMenu_Success() {
		onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
		onView(withId(R.id.rv_story)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
		onView(withId(R.id.rv_story)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5,
			click()
		))
		pressBack()
		onView(withId(R.id.fragment_location)).perform(click())
		onView(withId(R.id.view_map)).check(matches(isDisplayed()))
		onView(withId(R.id.fragment_setting)).perform(click())
		onView(withId(R.id.user_name)).check(matches(isDisplayed()))
	}
}