package com.naufaldystd.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.naufaldystd.core.R
import com.naufaldystd.core.databinding.ItemListStoryBinding
import com.naufaldystd.core.domain.model.Story
import com.naufaldystd.core.utils.DiffUtils

class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

	private var listStory = ArrayList<Story>()
	var onItemClick: ((Story) -> Unit)? = null

	fun setData(newListData: List<Story>?) {
		if (newListData == null) return
		val diffUtilCallback = DiffUtils(listStory, newListData)
		val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
		listStory.clear()
		listStory.addAll(newListData)
		diffResult.dispatchUpdatesTo(this)

	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.ListViewHolder =
		ListViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.item_list_story, parent, false)
		)

	override fun onBindViewHolder(holder: StoryAdapter.ListViewHolder, position: Int) {
		val data = listStory[position]
		holder.bind(data)
	}

	override fun getItemCount(): Int = listStory.size

	inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val binding = ItemListStoryBinding.bind(itemView)
		fun bind(data: Story) {
			with(binding) {
				Glide.with(itemView.context)
					.load(data.photoURL)
					.apply(
						RequestOptions.placeholderOf(R.drawable.ic_loading)
							.error(R.drawable.ic_error)
					)
					.into(ivItemImageList)
				tvUserAndParagraphList.text = HtmlCompat.fromHtml(
					itemView.context.getString(
						R.string.story_text_format,
						data.name,
						data.description
					), HtmlCompat.FROM_HTML_MODE_LEGACY
				)
				tvDatetimeList.text = data.createdAt
			}
		}

		init {
			binding.root.setOnClickListener {
				onItemClick?.invoke(listStory[adapterPosition])
			}
		}
	}

}