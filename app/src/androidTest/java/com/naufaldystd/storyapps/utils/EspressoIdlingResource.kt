package com.naufaldystd.storyapps.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

	private const val RESOURCE = "GLOBAL"

	@JvmField
	val countingIdlingResource = CountingIdlingResource(RESOURCE)
}

