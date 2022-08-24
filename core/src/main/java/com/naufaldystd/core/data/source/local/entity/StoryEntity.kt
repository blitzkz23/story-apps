package com.naufaldystd.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story")
data class StoryEntity(
	@PrimaryKey
	@NonNull
	@ColumnInfo(name = "id")
	val id: Int,

	@ColumnInfo(name = "name")
	val name: String,

	@ColumnInfo(name = "description")
	val description: String,

	@ColumnInfo(name = "photoURL")
	val photoURL: String,

	@ColumnInfo(name = "createdAt")
	val createdAt: String,

	@ColumnInfo(name = "lat")
	val lat: Double,

	@ColumnInfo(name = "lon")
	val lon: Double
)
