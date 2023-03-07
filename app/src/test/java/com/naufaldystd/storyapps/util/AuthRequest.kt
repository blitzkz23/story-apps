package com.naufaldystd.storyapps.util

data class AuthRequest(
	val name: String? = null,
	val email: String,
	val password: String
)
