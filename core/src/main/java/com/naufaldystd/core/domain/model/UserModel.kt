package com.naufaldystd.core.domain.model

data class UserModel(
    val name: String?,
    val userId: String,
    val token: String,
    val isLogin: Boolean
)