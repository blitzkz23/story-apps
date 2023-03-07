package com.naufaldystd.storyapps.ui.story.home.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.ContextWrapper
import android.content.Intent
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.naufaldystd.core.R
import com.naufaldystd.core.data.source.remote.response.StoryResponse
import com.naufaldystd.core.databinding.ItemListStoryBinding
import com.naufaldystd.core.utils.setLocalDateFormat
import com.naufaldystd.storyapps.ui.detail.DetailStoryActivity

class StoryAdapter : PagingDataAdapter<StoryResponse, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
		ListViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.item_list_story, parent, false)
		)

	override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
		val data = getItem(position)
		if (data != null) {
			holder.bind(data)
		}
	}

	inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val binding = ItemListStoryBinding.bind(itemView)
		fun bind(data: StoryResponse) {
			with(binding) {
				Glide.with(itemView.context)
					.load(data.photoUrl)
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
				tvDatetimeList.setLocalDateFormat(data.createdAt)
				val context = (itemView.context as ContextWrapper).baseContext
				val optionsCompat: ActivityOptions =
					ActivityOptions.makeSceneTransitionAnimation(
						context as Activity,
						Pair(ivItemImageList, "image"),
						Pair(tvUserAndParagraphList, "text"),
						Pair(tvDatetimeList, "datetime")
					)
				root.setOnClickListener {
					val intent = Intent(context, DetailStoryActivity::class.java)
					intent.putExtra(DetailStoryActivity.EXTRA_PARCEL, data)
					context.startActivity(intent, optionsCompat.toBundle())
				}
			}

		}
	}

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponse>() {
			override fun areItemsTheSame(
				oldItem: StoryResponse,
				newItem: StoryResponse
			): Boolean {
				return oldItem == newItem
			}

			override fun areContentsTheSame(
				oldItem: StoryResponse,
				newItem: StoryResponse
			): Boolean {
				return oldItem.id == newItem.id
			}
		}
	}


}