package com.naufaldystd.core.domain.model

data class Story(
	var id: String,
	var name: String,
	var description: String,
	var photoURL: String,
	var createdAt: String,
	var lat: Double, 
	var lon: Double
)
