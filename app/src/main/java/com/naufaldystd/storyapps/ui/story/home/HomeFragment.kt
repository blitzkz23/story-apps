package com.naufaldystd.storyapps.ui.story.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.naufaldystd.storyapps.R
import com.naufaldystd.storyapps.databinding.FragmentHomeBinding
import com.naufaldystd.storyapps.ui.auth.AuthActivity
import com.naufaldystd.storyapps.ui.detail.DetailStoryActivity
import com.naufaldystd.storyapps.ui.story.add.AddStoryActivity
import com.naufaldystd.storyapps.ui.story.home.adapter.LoadingStateAdapter
import com.naufaldystd.storyapps.ui.story.home.adapter.StoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!
	private val homeViewModel: HomeViewModel by viewModels()
	private lateinit var storyAdapter: StoryAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentHomeBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupButtonAction()
		setupAdapter()
		lifecycleScope.launch {
			homeViewModel.getUser().observe(viewLifecycleOwner) { user ->
				if (user.name == getString(R.string.tamu) || user.name == getString(R.string.guest)) {
					with(binding) {
						imageSorry.visibility = View.VISIBLE
						messageForGuest.visibility = View.VISIBLE
						btnRegister2.visibility = View.VISIBLE
						rvStory.visibility = View.GONE
					}
				} else {
					setupHeaderTokenAndStoryData()
				}
			}
		}
	}

	private fun setupButtonAction() {
		binding.btnAddStory.setOnClickListener {
			startActivity(Intent(requireActivity(), AddStoryActivity::class.java))
		}
		binding.btnRegister2.setOnClickListener {
			startActivity(Intent(requireActivity(), AuthActivity::class.java).also { intent ->
				intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			})
			activity?.finish()
		}
	}

	/**
	 * Set adapter for recyclerview
	 *
	 */
	private fun setupAdapter() {
		storyAdapter = StoryAdapter()
		storyAdapter.onItemClick = { intentData ->
			val image =
				activity?.findViewById<ImageView>(com.naufaldystd.core.R.id.iv_item_image_list)
			val text =
				activity?.findViewById<TextView>(com.naufaldystd.core.R.id.tv_user_and_paragraph_list)
			val datetime =
				activity?.findViewById<TextView>(com.naufaldystd.core.R.id.tv_datetime_list)
			val optionsCompat: ActivityOptionsCompat =
				ActivityOptionsCompat.makeSceneTransitionAnimation(
					requireActivity(),
					Pair(image, "image"),
					Pair(text, "text"),
					Pair(datetime, "datetime")
				)
			val intent = Intent(requireActivity(), DetailStoryActivity::class.java)
			intent.putExtra(DetailStoryActivity.EXTRA_PARCEL, intentData)
			startActivity(intent, optionsCompat.toBundle())
		}
	}

	/**
	 * Set header token for data request and set the returned data into adapter and eventually recyclerview
	 */
	private fun setupHeaderTokenAndStoryData() {
		binding.loading.visibility = View.VISIBLE
		lifecycleScope.launch {
			homeViewModel.getUser().observe(viewLifecycleOwner) { user ->
				homeViewModel.getAllStories(user.token).observe(viewLifecycleOwner) { story ->
					binding.loading.visibility = View.GONE
					storyAdapter.submitData(lifecycle, story)
				}
			}
		}

		with(binding.rvStory) {
			layoutManager = LinearLayoutManager(context)
			adapter = storyAdapter.withLoadStateFooter(
				footer = LoadingStateAdapter {
					storyAdapter.retry()
				}
			)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}