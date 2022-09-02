package com.naufaldystd.core.utils

import androidx.recyclerview.widget.DiffUtil
import com.naufaldystd.core.domain.model.Story

class DiffUtils(private val oldList: List<Story>, private val newList: List<Story>) :
	DiffUtil.Callback() {
	override fun getOldListSize(): Int = oldList.size

	override fun getNewListSize(): Int = newList.size

	override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		return oldList[oldItemPosition].id == newList[newItemPosition].id
	}

	override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
		val (id, name, description, photoUrl, createdAt, lat, lon) = oldList[oldItemPosition]
		val (id2, name2, description2, photoUrl2, createdAt2, lat2, lon2) = newList[newItemPosition]

		return id == id2
				&& name == name2
				&& description == description2
				&& photoUrl == photoUrl2
				&& createdAt == createdAt2
				&& lat == lat2
				&& lon == lon2
	}
}