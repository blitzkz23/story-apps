package com.naufaldystd.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
	var id: String,
	var name: String,
	var description: String,
	var photoURL: String,
	var createdAt: String,
	var lat: Double,
	var lon: Double
) : Parcelable
