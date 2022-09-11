package com.naufaldystd.core.utils

import com.naufaldystd.core.data.source.local.entity.StoryEntity
import com.naufaldystd.core.data.source.remote.response.LoginResult
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.domain.model.UserModel

object DataMapper {
	fun mapLoginResponsesToUserModel(input: LoginResult) = UserModel(
		name = input.name,
		userId = input.userId,
		token = input.token,
		isLogin = true
	)

	fun mapStoryResponsesToEntities(input: List<StoryResponse>): List<StoryEntity> =
		input.map {
			StoryEntity(
				id = it.id,
				name = it.name,
				description = it.description,
				photoUrl = it.photoUrl,
				createdAt = it.createdAt,
				lat = it.lat,
				lon = it.lon
			)
		}

	fun mapStoryEntitiesToStory(input: List<StoryEntity>): List<Story> =
		input.map {
			Story(
				id = it.id,
				name = it.name,
				description = it.description,
				photoURL = it.photoUrl,
				createdAt = it.createdAt,
				lat = it.lat,
				lon = it.lon
			)
		}

	fun mapResponsesToStory(input: List<StoryResponse>): List<Story> =
		input.map {
			Story(
				id = it.id,
				name = it.name,
				description = it.description,
				photoURL = it.photoUrl,
				createdAt = it.createdAt,
				lat = it.lat,
				lon = it.lon
			)
		}
}